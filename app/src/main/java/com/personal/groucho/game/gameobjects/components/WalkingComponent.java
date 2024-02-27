package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Utils.toMetersXLength;
import static com.personal.groucho.game.gameobjects.ComponentType.CHARACTER;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.gameobjects.Component;

public abstract class WalkingComponent extends Component {
    public PositionComponent posComp = null;
    protected SpriteDrawableComponent spriteComp = null;
    protected PhysicsComponent phyComp = null;
    public CharacterComponent character = null;
    protected float increaseX, increaseY;


    protected void walking() {
        initComponents();

        switch (posComp.orientation) {
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

    public void updateSprite(Spritesheet sheet) {
        initComponents();

        spriteComp.setCurrentSpritesheet(sheet);
        spriteComp.setAnim(posComp.orientation.getValue());
    }

    private void updatePosition(float increaseX, float increaseY) {
        initComponents();

        phyComp.updatePosX(increaseX);
        phyComp.updatePosY(increaseY);
    }

    public void initComponents() {
        if (posComp == null) {
            posComp = (PositionComponent) owner.getComponent(POSITION);
        }
        if(phyComp == null) {
            phyComp = (PhysicsComponent) owner.getComponent(PHYSICS);
        }
        if (spriteComp == null) {
            spriteComp = (SpriteDrawableComponent) owner.getComponent(DRAWABLE);
        }
        if (character == null){
            character = (CharacterComponent) owner.getComponent(CHARACTER);
        }
    }
}
