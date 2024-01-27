package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.characterDimensionsX;
import static com.personal.groucho.game.Constants.characterDimensionsY;
import static com.personal.groucho.game.Constants.maxLightIntensity;
import static com.personal.groucho.game.Constants.minLightIntensity;
import static com.personal.groucho.game.Constants.speed;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.assets.Sounds.click;
import static com.personal.groucho.game.assets.Sounds.loading;

import com.personal.groucho.game.controller.ControllerObserver;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.assets.Sounds;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.states.ControllerState;
import com.personal.groucho.game.controller.states.NameState;

public class ControllableComponent extends Component implements ControllerObserver {

    private final Controller controller;
    private final GameWorld gameworld;
    private SpriteDrawableComponent spriteComponent = null;
    private PhysicsComponent physicsComponent = null;
    private LightComponent lightComponent = null;

    private boolean playShotAnimation = false;
    private boolean playLoadingSound = true;

    public ControllableComponent(Controller controller, GameWorld gameworld) {
        this.controller = controller;
        this.gameworld = gameworld;
    }

    @Override
    public ComponentType type() { return ComponentType.Controllable;}

    public void updatePlayerState() {
        if (spriteComponent == null)
            spriteComponent = (SpriteDrawableComponent) owner.getComponent(ComponentType.Drawable);
        if (lightComponent == null)
            lightComponent = (LightComponent) owner.getComponent(ComponentType.Light);

        if (controller.getPlayerState().getName() == NameState.WALKING)
            handleWalkingPlayer();
        else if (controller.getPlayerState().getName() == NameState.AIMING)
            handleAimingPlayer();

        if (playShotAnimation) {
            updateSprite(Spritesheets.groucho_fire, controller.getOrientation());
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
        updateSprite(Spritesheets.groucho_idle, controller.getOrientation());
    }

    private void handleWalkingPlayer() {
        playLoadingSound = true;
        if(physicsComponent == null)
            physicsComponent = (PhysicsComponent) owner.getComponent(ComponentType.Physics);

        updateSprite(Spritesheets.groucho_walk, controller.getOrientation());

        switch (controller.getOrientation()) {
            case UP:
                updatePosition(0, speed*(-1));
                break;
            case DOWN:
                updatePosition(0,speed*(1));
                break;
            case LEFT:
                updatePosition(speed*(-1),0);
                break;
            case RIGHT:
                updatePosition(speed*(1),0);
                break;
        }
    }

    private void handleAimingPlayer() {
        updateSprite(Spritesheets.groucho_aim, controller.getOrientation());
    }

    private void handleLoadingPlayer() {
        if (playLoadingSound) {
            loading.play(0.4f);
            playLoadingSound = false;
        }
        updateSprite(Spritesheets.groucho_aim, controller.getOrientation());
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
            physicsComponent = (PhysicsComponent) owner.getComponent(ComponentType.Physics);

        float originX = fromMetersToBufferX(physicsComponent.getPositionX());
        float originY = fromMetersToBufferY(physicsComponent.getPositionY());
        float endX = 0;
        float endY = 0;

        switch (controller.getOrientation()) {
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

    private void updatePosition(float increaseX, float increaseY) {
        physicsComponent.updatePosX(increaseX);
        physicsComponent.updatePosY(increaseY);
    }

    private void updateSprite(Spritesheet sheet, Orientation orientation) {
        spriteComponent.setCurrentSpritesheet(sheet);
        spriteComponent.setAnimation(orientation.getValue());
    }

    public void update(ControllerState currentState) {
        if (spriteComponent == null)
            spriteComponent = (SpriteDrawableComponent) owner.getComponent(ComponentType.Drawable);
        if (lightComponent == null)
            lightComponent = (LightComponent) owner.getComponent(ComponentType.Light);

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
