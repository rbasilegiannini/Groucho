package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.grouchoSpeed;

import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public abstract class WalkingComponent extends Component {
    protected PositionComponent positionComponent = null;
    protected SpriteDrawableComponent spriteComponent = null;
    protected PhysicsComponent physicsComponent = null;

    protected void walking(Spritesheet sheet) {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        updateSprite(sheet);

        switch (positionComponent.getOrientation()) {
            case UP:
                updatePosition(0, grouchoSpeed *(-1));
                break;
            case DOWN:
                updatePosition(0, grouchoSpeed *(1));
                break;
            case LEFT:
                updatePosition(grouchoSpeed *(-1),0);
                break;
            case RIGHT:
                updatePosition(grouchoSpeed *(1),0);
                break;
        }
    }

    protected void updateSprite(Spritesheet sheet) {
        if (spriteComponent == null)
            spriteComponent = (SpriteDrawableComponent) owner.getComponent(ComponentType.Drawable);
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        spriteComponent.setCurrentSpritesheet(sheet);
        spriteComponent.setAnimation(positionComponent.getOrientation().getValue());
    }

    private void updatePosition(float increaseX, float increaseY) {
        if(physicsComponent == null)
            physicsComponent = (PhysicsComponent) owner.getComponent(ComponentType.Physics);

        physicsComponent.updatePosX(increaseX);
        physicsComponent.updatePosY(increaseY);
    }
}
