package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Utils.toMetersXLength;
import static com.personal.groucho.game.gameobjects.ComponentType.CHARACTER;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.gameobjects.Component;

public abstract class WalkingComponent extends Component {
    protected PositionComponent posComponent = null;
    protected SpriteDrawableComponent spriteComponent = null;
    protected PhysicsComponent phyComponent = null;
    protected CharacterComponent character = null;
    protected float increaseX, increaseY;


    protected void walking() {
        initComponents();

        switch (posComponent.orientation) {
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
        updatePosition(
                toMetersXLength(character.properties.speed) * increaseX,
                toMetersXLength(character.properties.speed) * increaseY);
        updateSprite(character.properties.sheetWalk);
    }

    protected void updateSprite(Spritesheet sheet) {
        initComponents();

        spriteComponent.setCurrentSpritesheet(sheet);
        spriteComponent.setAnim(posComponent.orientation.getValue());
    }

    private void updatePosition(float increaseX, float increaseY) {
        initComponents();

        phyComponent.updatePosX(increaseX);
        phyComponent.updatePosY(increaseY);
    }

    private void initComponents() {
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }
        if(phyComponent == null) {
            phyComponent = (PhysicsComponent) owner.getComponent(PHYSICS);
        }
        if (spriteComponent == null) {
            spriteComponent = (SpriteDrawableComponent) owner.getComponent(DRAWABLE);
        }
        if (character == null){
            character = (CharacterComponent) owner.getComponent(CHARACTER);
        }
    }
}
