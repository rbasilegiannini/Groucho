package com.personal.groucho.game.components;

import com.personal.groucho.game.AnimationType;
import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.Spritesheets;

public class ControllableComponent extends Component {

    private final Controller controller;

    public ControllableComponent(Controller controller) {
        this.controller = controller;
    }
    @Override
    public ComponentType type() { return ComponentType.Controllable;}

    public void updatePlayerState() {
        SpriteDrawableComponent spriteDrawableComponent =
                (SpriteDrawableComponent) owner.getComponent(ComponentType.Drawable);
        PositionComponent positionComponent =
                (PositionComponent) owner.getComponent(ComponentType.Position);

        if(controller.isUpPressed()) {
            updateSprite(spriteDrawableComponent, Spritesheets.groucho_walk, AnimationType.UP);
            // Change position
        }
        if(controller.isDownPressed()) {
            updateSprite(spriteDrawableComponent, Spritesheets.groucho_walk, AnimationType.DOWN);
            // Change position
        }
        if(controller.isLeftPressed()) {
            updateSprite(spriteDrawableComponent, Spritesheets.groucho_walk, AnimationType.LEFT);
            // Change position
        }
        if(controller.isRightPressed()) {
            updateSprite(spriteDrawableComponent, Spritesheets.groucho_walk, AnimationType.RIGHT);
            // Change position
        }
    }

    private void updateSprite(SpriteDrawableComponent spriteComponent, Spritesheet sheet, int animation) {
        spriteComponent.setSpritesheet(sheet);
        spriteComponent.setAnimation(animation);
    }
}
