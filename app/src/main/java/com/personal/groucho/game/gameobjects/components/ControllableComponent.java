package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.constants.System.characterDimensionsX;
import static com.personal.groucho.game.constants.System.characterDimensionsY;
import static com.personal.groucho.game.constants.CharacterProperties.grouchoSpeed;
import static com.personal.groucho.game.constants.Environment.maxLightIntensity;
import static com.personal.groucho.game.constants.Environment.minLightIntensity;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.assets.Sounds.click;
import static com.personal.groucho.game.assets.Sounds.loading;
import static com.personal.groucho.game.assets.Spritesheets.grouchoWalk;

import com.personal.groucho.game.controller.ControllerObserver;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Sounds;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.states.ControllerState;
import com.personal.groucho.game.controller.states.NameState;
import com.personal.groucho.game.gameobjects.ComponentType;

public class ControllableComponent extends WalkingComponent implements ControllerObserver {

    private final Controller controller;
    private final GameWorld gameworld;
    private LightComponent lightComponent = null;

    private boolean playShotAnimation = false;
    private boolean playLoadingSound = true;

    public ControllableComponent(Controller controller, GameWorld gameworld) {
        this.controller = controller;
        this.gameworld = gameworld;
    }

    @Override
    public ComponentType type() { return ComponentType.CONTROLLABLE;}

    public void updatePlayerState() {
        if (lightComponent == null)
            lightComponent = (LightComponent) owner.getComponent(ComponentType.LIGHT);

        if (controller.getPlayerState().getName() == NameState.WALKING)
            handleWalkingPlayer();
        else if (controller.getPlayerState().getName() == NameState.AIMING)
            handleAimingPlayer();

        if (playShotAnimation) {
            updateSprite(Spritesheets.grouchoFire);
            playShotAnimation = false;
        }
    }

    public void switchLightEvent(boolean isTurnOn) {
        click.play(1f);
        if(isTurnOn)
            lightComponent.setLightIntensity(maxLightIntensity);
        else
            lightComponent.setLightIntensity(minLightIntensity);

    }

    private void handleIdlePlayer() {
        playLoadingSound = true;
        updateSprite(Spritesheets.grouchoIdle);
    }

    private void handleWalkingPlayer() {
        playLoadingSound = true;
        walking(grouchoWalk, grouchoSpeed);
    }

    private void handleAimingPlayer() {
        updateSprite(Spritesheets.grouchoAim);
    }

    private void handleLoadingPlayer() {
        if (playLoadingSound) {
            loading.play(0.4f);
            playLoadingSound = false;
        }
        updateSprite(Spritesheets.grouchoAim);
    }

    private void handleShootingPlayer() {
        playShotAnimation = true;
        playLoadingSound = true;
        Sounds.shooting.play(0.2f);

        controller.consumeShoot();
        shoot();
    }

    private void shoot() {
        if(physicsComponent == null)
            physicsComponent = (PhysicsComponent) owner.getComponent(ComponentType.PHYSICS);

        float originX = fromMetersToBufferX(physicsComponent.getPositionX());
        float originY = fromMetersToBufferY(physicsComponent.getPositionY());
        float endX = 0;
        float endY = 0;

        switch (positionComponent.getOrientation()) {
            case UP:
                originY = originY - characterDimensionsY;
                endX = originX;
                endY = originY - 2000;
                break;
            case DOWN:
                originY = originY + characterDimensionsY;
                endX = originX;
                endY = originY + 2000;
                break;
            case LEFT:
                originX = originX - characterDimensionsX;
                endX = originX - 2000;
                endY = originY;
                break;
            case RIGHT:
                originX = originX + characterDimensionsX;
                endX = originX + 2000;
                endY = originY;
                break;
        }
        gameworld.shootEvent(originX, originY, endX, endY);
    }

    @Override
    public void update(ControllerState currentState) {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.POSITION);

        positionComponent.setOrientation(controller.getOrientation());

        switch (currentState.getName()) {
            case IDLE:
                handleIdlePlayer();
                break;
            case AIMING:
                handleAimingPlayer();
                break;
            case LOADING:
                handleLoadingPlayer();
                break;
            case SHOOTING:
                handleShootingPlayer();
                break;
        }
    }
}
