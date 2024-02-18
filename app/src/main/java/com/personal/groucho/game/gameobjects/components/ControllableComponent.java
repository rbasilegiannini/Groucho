package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Events.turnOffLightEvent;
import static com.personal.groucho.game.Events.turnOnLightEvent;
import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterDimY;
import static com.personal.groucho.game.constants.Environment.maxLightIntensity;
import static com.personal.groucho.game.constants.Environment.minLightIntensity;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.assets.Sounds.loading;
import static com.personal.groucho.game.controller.states.StateName.AIMING;
import static com.personal.groucho.game.controller.states.StateName.WALKING;
import static com.personal.groucho.game.gameobjects.ComponentType.CHARACTER;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.LIGHT;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import com.personal.groucho.game.controller.ControllerObserver;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Sounds;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.states.ControllerState;
import com.personal.groucho.game.gameobjects.ComponentType;

public class ControllableComponent extends WalkingComponent implements ControllerObserver {

    private final Controller controller;
    private final GameWorld gameworld;
    private LightComponent lightComponent = null;
    private CharacterComponent character = null;
    private boolean playShotAnimation = false;
    private boolean playLoadingSound = true;

    public ControllableComponent(GameWorld gameworld) {
        this.gameworld = gameworld;
        this.controller = gameworld.controller;
    }

    @Override
    public ComponentType type() {return CONTROLLABLE;}

    public void updatePlayerState() {
        initComponents();

        if (controller.currentState.getName() == WALKING)
            handleWalkingPlayer();
        else if (controller.currentState.getName() == AIMING)
            handleAimingPlayer();

        if (playShotAnimation) {
            updateSprite(character.properties.sheetFire);
            playShotAnimation = false;
        }
    }

    private void initComponents() {
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }
        if(phyComponent == null) {
            phyComponent = (PhysicsComponent) owner.getComponent(PHYSICS);
        }
        if (lightComponent == null) {
            lightComponent = (LightComponent) owner.getComponent(LIGHT);
        }
        if (character == null) {
            character = (CharacterComponent) owner.getComponent(CHARACTER);
        }
    }

    public void switchLightEvent(boolean isTurnOn) {
        if(isTurnOn) {
            lightComponent.setLightIntensity(maxLightIntensity);
            turnOnLightEvent(gameworld);
        }
        else {
            lightComponent.setLightIntensity(minLightIntensity);
            turnOffLightEvent(gameworld);
        }
    }

    private void handleIdlePlayer() {
        playLoadingSound = true;
        updateSprite(character.properties.sheetIdle);
    }

    private void handleWalkingPlayer() {
        playLoadingSound = true;
        walking();
    }

    private void handleAimingPlayer() {
        updateSprite(character.properties.sheetAim);
    }

    private void handleLoadingPlayer() {
        if (playLoadingSound) {
            loading.play(0.4f);
            playLoadingSound = false;
        }
        updateSprite(character.properties.sheetAim);
    }

    private void handleShootingPlayer() {
        playShotAnimation = true;
        playLoadingSound = true;
        Sounds.shooting.play(0.2f);

        controller.consumeShoot();
        shoot();
    }

    private void shoot() {
        initComponents();

        float originX = fromMetersToBufferX(phyComponent.getPosX());
        float originY = fromMetersToBufferY(phyComponent.getPosY());
        float endX = 0;
        float endY = 0;

        switch (posComponent.orientation) {
            case UP:
                originY = originY - characterDimY;
                endX = originX;
                endY = originY - 2000;
                break;
            case DOWN:
                originY = originY + characterDimY;
                endX = originX;
                endY = originY + 2000;
                break;
            case LEFT:
                originX = originX - characterDimX;
                endX = originX - 2000;
                endY = originY;
                break;
            case RIGHT:
                originX = originX + characterDimX;
                endX = originX + 2000;
                endY = originY;
                break;
        }
        gameworld.shootEvent(originX, originY, endX, endY);
    }

    @Override
    public void update(ControllerState currentState) {
        initComponents();

        posComponent.setOrientation(controller.orientation);

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
