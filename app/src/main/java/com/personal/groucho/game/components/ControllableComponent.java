package com.personal.groucho.game.components;

import android.util.Log;

import com.personal.groucho.game.Orientation;
import com.personal.groucho.game.Controller;
import com.personal.groucho.game.PlayerSounds;
import com.personal.groucho.game.animation.Spritesheet;
import com.personal.groucho.game.animation.Spritesheets;
import com.personal.groucho.game.states.Aiming;
import com.personal.groucho.game.states.ControllerState;
import com.personal.groucho.game.states.Idle;
import com.personal.groucho.game.states.Shooting;
import com.personal.groucho.game.states.Walking;

public class ControllableComponent extends Component {

    private final Controller controller;
    private SpriteDrawableComponent spriteComponent = null;
    private PositionComponent positionComponent = null;

    public ControllableComponent(Controller controller) {
        this.controller = controller;
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
        updatePosition(0, 0);
    }

    private void handleAimingPlayer() {
        updateSprite(Spritesheets.groucho_aim, controller.getOrientation());
    }

    private void handleShootingPlayer() {
        controller.consumeShoot();
        PlayerSounds.shootingSound.play(1f);
        // Shot sprite
        // Shot effect
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
