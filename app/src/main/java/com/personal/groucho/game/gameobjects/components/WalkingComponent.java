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
    protected SpriteComponent spriteComp = null;
    protected PhysicsComponent phyComp = null;
    public CharacterComponent charComp = null;
    protected float increaseX, increaseY;

    @Override
    public void reset() {
        super.reset();
        this.posComp = null;
        this.spriteComp = null;
        this.phyComp = null;
        this.charComp = null;
    }

    protected void walking(float elapsedTime) {
        initComponents();

        float speedPerSecond = charComp.properties.speed;

        // TODO: a temporary (non) solution to remove the stuttering.
        elapsedTime = 1;

        switch (posComp.orientation) {
            case UP:
                increaseX = 0;
                increaseY = -elapsedTime;
                break;
            case DOWN:
                increaseX = 0;
                increaseY = elapsedTime;
                break;
            case LEFT:
                increaseX = -elapsedTime;
                increaseY = 0;
                break;
            case RIGHT:
                increaseX = elapsedTime;
                increaseY = 0;
                break;
        }
//        updatePhysicsPos(
//                speedPerSecond*increaseX,
//                speedPerSecond*increaseY);
        updatePhysicsPos(
                toMetersXLength(speedPerSecond) * increaseX,
                toMetersXLength(speedPerSecond) * increaseY);
        updateSprite(charComp.properties.sheetWalk);
    }

    public void updateSprite(Spritesheet sheet) {
        initComponents();

        spriteComp.setCurrentSpritesheet(sheet);
        spriteComp.setAnim(posComp.orientation.getValue());
    }

    private void updatePhysicsPos(float increaseX, float increaseY) {
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
            spriteComp = (SpriteComponent) owner.getComponent(DRAWABLE);
        }
        if (charComp == null){
            charComp = (CharacterComponent) owner.getComponent(CHARACTER);
        }
    }
}
