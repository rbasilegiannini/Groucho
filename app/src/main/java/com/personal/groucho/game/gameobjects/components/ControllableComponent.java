package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Events.pauseEvent;
import static com.personal.groucho.game.Events.turnOffLightEvent;
import static com.personal.groucho.game.Events.turnOnLightEvent;
import static com.personal.groucho.game.constants.System.charDimX;
import static com.personal.groucho.game.constants.System.charDimY;
import static com.personal.groucho.game.constants.Environment.maxLightIntensity;
import static com.personal.groucho.game.constants.Environment.minLightIntensity;
import static com.personal.groucho.game.assets.Sounds.loading;
import static com.personal.groucho.game.constants.System.charScaleFactor;
import static com.personal.groucho.game.controller.states.StateName.AIMING;
import static com.personal.groucho.game.controller.states.StateName.WALKING;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.LIGHT;

import com.personal.groucho.game.controller.ControllerObserver;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Sounds;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.states.ControllerState;
import com.personal.groucho.game.gameobjects.ComponentType;

public class ControllableComponent extends WalkingComponent implements ControllerObserver {

    private Controller controller;
    private GameWorld gameWorld;
    private LightComponent lightComp = null;
    private boolean playShotAnim = false;
    private boolean playLoadingSound = true;

    public ControllableComponent() {}

    public void init(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.controller = gameWorld.controller;
    }

    @Override
    public void reset() {
        super.reset();
        this.controller = null;
        this.lightComp = null;
    }

    @Override
    public ComponentType type() {return CONTROLLABLE;}

    public void updatePlayerState(float elapsedTime) {
        initComponents();

        if (controller.currentState.getName() == WALKING)
            handleWalkingPlayer(elapsedTime);
        else if (controller.currentState.getName() == AIMING)
            handleAimingPlayer();

        if (playShotAnim) {
            updateSprite(charComp.properties.sheetFire);
            playShotAnim = false;
        }
    }

    @Override
    public void initComponents() {
        super.initComponents();
        if (lightComp == null) {
            lightComp = (LightComponent) owner.getComponent(LIGHT);
        }
    }

    @Override
    public void switchLight(boolean isTurnOn) {
        if(isTurnOn) {
            lightComp.setLightIntensity(maxLightIntensity);
            turnOnLightEvent(gameWorld);
        }
        else {
            lightComp.setLightIntensity(minLightIntensity);
            turnOffLightEvent(gameWorld);
        }
    }

    @Override
    public void pressPause() {
        pauseEvent(gameWorld);
    }

    private void handleIdlePlayer() {
        playLoadingSound = true;
        updateSprite(charComp.properties.sheetIdle);
    }

    private void handleWalkingPlayer(float elapsedTime) {
        playLoadingSound = true;
        walking(elapsedTime);
    }

    private void handleAimingPlayer() {
        updateSprite(charComp.properties.sheetAim);
    }

    private void handleLoadingPlayer() {
        if (playLoadingSound) {
            loading.play(0.4f);
            playLoadingSound = false;
        }
        updateSprite(charComp.properties.sheetAim);
    }

    private void handleShootingPlayer() {
        playShotAnim = true;
        playLoadingSound = true;
        Sounds.shooting.play(0.2f);

        controller.consumeShoot();
        shoot();
    }

    private void shoot() {
        initComponents();

        float originX = posComp.posX;
        float originY = posComp.posY;
        float endX = 0;
        float endY = 0;

        switch (posComp.orientation) {
            case UP:
                originY = originY - charDimY;
                endX = originX;
                endY = originY - 2000;
                break;
            case DOWN:
                originY = originY - charDimY;
                endX = originX;
                endY = originY + 2000;
                break;
            case LEFT:
                originX = originX + charDimX;
                originY = originY - (charScaleFactor-2)*charDimY;
                endX = originX - 2000;
                endY = originY;
                break;
            case RIGHT:
                originX = originX - charDimX;
                originY = originY - (charScaleFactor-2)*charDimY;
                endX = originX + 2000;
                endY = originY;
                break;
        }
        gameWorld.shootEvent(originX, originY, endX, endY);
    }

    @Override
    public void update(ControllerState currentState) {
        initComponents();

        posComp.setOrientation(controller.orientation);

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
