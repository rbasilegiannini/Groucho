package com.personal.groucho.game.components;

import android.util.Log;

import com.personal.groucho.game.Orientation;
import com.personal.groucho.game.Controller;
import com.personal.groucho.game.animation.Spritesheet;
import com.personal.groucho.game.animation.Spritesheets;

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

        switch (controller.getPlayerState()) {
            case IDLE:
                handleIdlePlayer(spriteComponent);
                break;
            case WALKING:
                handleWalkingPlayer(spriteComponent);
                break;
            case AIMING:
                handleAimingPlayer(spriteComponent);
                break;
            case SHOOTING:
                handleShootingPlayer(spriteComponent);
                break;
        }
    }

    private void handleIdlePlayer(SpriteDrawableComponent spriteComponent) {
        updateSprite(spriteComponent, Spritesheets.groucho_idle, controller.getOrientation());
    }

    private void handleWalkingPlayer(SpriteDrawableComponent spriteComponent) {
        if(positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        updateSprite(spriteComponent, Spritesheets.groucho_walk, controller.getOrientation());
        updatePosition(positionComponent, 0, 0);
    }

    private void handleAimingPlayer(SpriteDrawableComponent spriteComponent) {
        updateSprite(spriteComponent, Spritesheets.groucho_aim, controller.getOrientation());
    }

    private void handleShootingPlayer(SpriteDrawableComponent spriteComponent) {
        controller.consumeShoot();
        Log.i("Controller", "Fire!");
    }

    private void updatePosition(PositionComponent positionComponent, int increaseX, int increaseY) {
        positionComponent.updatePosX(increaseX);
        positionComponent.updatePosY(increaseY);
    }

    private void updateSprite(SpriteDrawableComponent spriteComponent, Spritesheet sheet, Orientation orientation) {
        spriteComponent.setSpritesheet(sheet);
        spriteComponent.setAnimation(orientation.getValue());
    }
}
