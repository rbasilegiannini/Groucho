package com.personal.groucho.game.gameobjects.components;

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
    protected float phyIncreaseX, phyIncreaseY;

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

        float phySpeedPerSecond = charComp.properties.speed;

        switch (posComp.orientation) {
            case UP:
                phyIncreaseX = 0;
                phyIncreaseY = -(phySpeedPerSecond*elapsedTime);
                break;
            case DOWN:
                phyIncreaseX = 0;
                phyIncreaseY = phySpeedPerSecond*elapsedTime;
                break;
            case LEFT:
                phyIncreaseX = -(phySpeedPerSecond*elapsedTime);
                phyIncreaseY = 0;
                break;
            case RIGHT:
                phyIncreaseX = phySpeedPerSecond*elapsedTime;
                phyIncreaseY = 0;
                break;
        }
        updatePhysicsPos(phyIncreaseX, phyIncreaseY);

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
