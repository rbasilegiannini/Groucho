package com.personal.groucho.game.components;

import android.util.Log;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Orientation;
import com.personal.groucho.game.Controller;
import com.personal.groucho.game.assets.PlayerSounds;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.states.Aiming;
import com.personal.groucho.game.states.ControllerState;
import com.personal.groucho.game.states.Idle;
import com.personal.groucho.game.states.Loading;
import com.personal.groucho.game.states.Shooting;
import com.personal.groucho.game.states.Walking;

public class ControllableComponent extends Component {

    private final Controller controller;
    private final GameWorld gameworld;
    private SpriteDrawableComponent spriteComponent = null;
    private PositionComponent positionComponent = null;
    private boolean canLoadingSound;

    public ControllableComponent(Controller controller, GameWorld gameworld) {
        this.controller = controller;
        this.gameworld = gameworld;
    }

    @Override
    public ComponentType type() { return ComponentType.Controllable;}

    public void updatePlayerState() {
        if (spriteComponent == null)
            spriteComponent = (SpriteDrawableComponent) owner.getComponent(ComponentType.Drawable);

        Class<? extends ControllerState> aClass = controller.getPlayerState().getClass();
        if (aClass.equals(Idle.class)) {
            handleIdlePlayer();
        } else if (aClass.equals(Walking.class)) {
            handleWalkingPlayer();
        } else if (aClass.equals(Aiming.class)) {
            handleAimingPlayer();
        } else if (aClass.equals(Loading.class)) {
            handleLoadingPlayer();
        } else if (aClass.equals(Shooting.class)) {
            handleShootingPlayer();
        }
    }

    private void handleIdlePlayer() {
        updateSprite(Spritesheets.groucho_idle, controller.getOrientation());
    }

    private void handleWalkingPlayer() {
        if(positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        updateSprite(Spritesheets.groucho_walk, controller.getOrientation());

        int speed = 1;
        switch (controller.getOrientation()) {
            case UP:
                updatePosition(0, speed*(-10));
                break;
            case DOWN:
                updatePosition(0,speed*(10));
                break;
            case LEFT:
                updatePosition(speed*(-10),0);
                break;
            case RIGHT:
                updatePosition(speed*(10),0);
                break;
        }
    }

    private void handleAimingPlayer() {
        canLoadingSound = true;
        updateSprite(Spritesheets.groucho_aim, controller.getOrientation());
    }

    private void handleLoadingPlayer() {
        if (canLoadingSound) {
            PlayerSounds.loadingSound.play(1f);
            canLoadingSound = false;
        }
        updateSprite(Spritesheets.groucho_aim, controller.getOrientation());
    }

    private void handleShootingPlayer() {
        controller.consumeShoot();
        PlayerSounds.shootingSound.play(1f);
        updateSprite(Spritesheets.groucho_fire, controller.getOrientation());
        shot();
    }

    private void shot() {
        if(positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        float originX = positionComponent.getPosX();
        float originY = positionComponent.getPosY();
        float endX = 0;
        float endY = 0;

        switch (controller.getOrientation()) {
            case UP:
                endX = originX;
                endY = originY - 1000;
                break;
            case DOWN:
                endX = originX;
                endY = originY + 1000;
                break;
            case LEFT:
                endX = originX - 1000;
                endY = originY;
                break;
            case RIGHT:
                endX = originX + 1000;
                endY = originY;
                break;
        }
        gameworld.shootEvent(originX, originY, endX, endY);
        Log.i("Controller", "Fire!");
    }

    private void updatePosition(int increaseX, int increaseY) {
        positionComponent.updatePosX(increaseX);
        positionComponent.updatePosY(increaseY);
    }

    private void updateSprite(Spritesheet sheet, Orientation orientation) {
        spriteComponent.setSpritesheet(sheet);
        spriteComponent.setAnimation(orientation.getValue());
    }
}
