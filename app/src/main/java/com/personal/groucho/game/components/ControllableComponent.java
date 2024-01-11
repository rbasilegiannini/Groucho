package com.personal.groucho.game.components;

import com.personal.groucho.game.animation.AnimationType;
import com.personal.groucho.game.Controller;
import com.personal.groucho.game.animation.Spritesheet;
import com.personal.groucho.game.animation.Spritesheets;

public class ControllableComponent extends Component {

    private final Controller controller;
    private int currentAnimationType;

    public ControllableComponent(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ComponentType type() { return ComponentType.Controllable;}

    public void updatePlayerState() {
        SpriteDrawableComponent spriteComponent =
                (SpriteDrawableComponent) owner.getComponent(ComponentType.Drawable);

        if (controller.isIdle()) {
            handleIdlePlayer(spriteComponent);
        }
        else  {
            handleActivePlayer(spriteComponent);
        }
    }

    private void handleActivePlayer(SpriteDrawableComponent spriteComponent) {
        PositionComponent positionComponent =
                (PositionComponent) owner.getComponent(ComponentType.Position);

        if (controller.isUpPressed()) {
            updateSprite(spriteComponent, Spritesheets.groucho_walk, AnimationType.UP);
            updatePosition(positionComponent, 0, 0);
        }
        if (controller.isDownPressed()) {
            updateSprite(spriteComponent, Spritesheets.groucho_walk, AnimationType.DOWN);
            updatePosition(positionComponent, 0, 0);
        }
        if (controller.isLeftPressed()) {
            updateSprite(spriteComponent, Spritesheets.groucho_walk, AnimationType.LEFT);
            updatePosition(positionComponent, 0, 0);
        }
        if (controller.isRightPressed()) {
            updateSprite(spriteComponent, Spritesheets.groucho_walk, AnimationType.RIGHT);
            updatePosition(positionComponent, 0, 0);
        }
    }

    private void updatePosition(PositionComponent positionComponent, int increaseX, int increaseY) {
        positionComponent.updatePosX(increaseX);
        positionComponent.updatePosY(increaseY);
    }

    private void updateSprite(SpriteDrawableComponent spriteComponent, Spritesheet sheet, int animation) {
        spriteComponent.setSpritesheet(sheet);
        spriteComponent.setAnimation(animation);
        currentAnimationType = animation;
    }

    private void handleIdlePlayer(SpriteDrawableComponent spriteComponent) {
        switch (currentAnimationType) {
            case AnimationType.UP:
                updateSprite(spriteComponent, Spritesheets.groucho_idle, AnimationType.UP);
                break;
            case AnimationType.DOWN:
                updateSprite(spriteComponent, Spritesheets.groucho_idle, AnimationType.DOWN);
                break;
            case AnimationType.LEFT:
                updateSprite(spriteComponent, Spritesheets.groucho_idle, AnimationType.LEFT);
                break;
            case AnimationType.RIGHT:
                updateSprite(spriteComponent, Spritesheets.groucho_idle, AnimationType.RIGHT);
                break;
        }
    }
}
