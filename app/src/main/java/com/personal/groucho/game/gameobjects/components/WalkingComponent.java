package com.personal.groucho.game.gameobjects.components;

import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public abstract class WalkingComponent extends Component {
    protected PositionComponent positionComponent = null;
    protected SpriteDrawableComponent spriteComponent = null;
    protected PhysicsComponent physicsComponent = null;
    protected float increaseX, increaseY;


    protected void walking(Spritesheet sheet, float speed) {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.POSITION);

        switch (positionComponent.getOrientation()) {
            case UP:
                increaseX = 0;
                increaseY = -1;
                break;
            case DOWN:
                increaseX = 0;
                increaseY = 1;
                break;
            case LEFT:
                increaseX = -1;
                increaseY = 0;
                break;
            case RIGHT:
                increaseX = 1;
                increaseY = 0;
                break;
        }
        updatePosition(speed*increaseX, speed * increaseY);
        updateSprite(sheet);
    }

    protected void updateSprite(Spritesheet sheet) {
        if (spriteComponent == null)
            spriteComponent = (SpriteDrawableComponent) owner.getComponent(ComponentType.DRAWABLE);
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.POSITION);

        spriteComponent.setCurrentSpritesheet(sheet);
        spriteComponent.setAnimation(positionComponent.getOrientation().getValue());
    }

    private void updatePosition(float increaseX, float increaseY) {
        if(physicsComponent == null)
            physicsComponent = (PhysicsComponent) owner.getComponent(ComponentType.PHYSICS);

        physicsComponent.updatePosX(increaseX);
        physicsComponent.updatePosY(increaseY);
    }
}
