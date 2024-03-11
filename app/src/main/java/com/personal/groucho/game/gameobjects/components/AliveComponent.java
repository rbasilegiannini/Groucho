package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.CHARACTER;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Status;

public class AliveComponent extends Component {
    private int maxHealth;
    public int currentHealth;
    public Status currentStatus;
    private SpriteComponent spriteComp = null;
    private CharacterComponent charComp = null;
    private GameWorld gameWorld;

    public AliveComponent() {}
    public void init(GameWorld gameWorld, int maxHealth) {
        currentStatus = Status.ALIVE;
        this.gameWorld = gameWorld;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    @Override
    public void reset() {
        super.reset();
        this.spriteComp = null;
        this.charComp = null;
    }

    @Override
    public ComponentType type() {return ALIVE;}

    public void damage(int power) {
        initComponents();

        currentHealth -= power;
        if (currentHealth <= 0) {
            currentHealth = 0;
            die();
        }
        updateSprite();
    }

    public void heal(int medicalKit) {
        initComponents();

        if (currentHealth+medicalKit <= maxHealth)
            currentHealth += medicalKit;
        else
            currentHealth = maxHealth;
        updateSprite();
    }

    private void die() {
        currentStatus = DEAD;
        spriteComp.setCurrentSpritesheet(charComp.properties.sheetDeath);
        spriteComp.setAnim(0);
        gameWorld.handleDeath((GameObject) getOwner());
    }

    private void updateSprite() {
        initComponents();
        spriteComp.updateColorFilter(currentHealth, maxHealth);
    }

    private void initComponents(){
        if (charComp == null) {
            charComp = (CharacterComponent) owner.getComponent(CHARACTER);
        }
        if (spriteComp == null) {
            spriteComp = (SpriteComponent) owner.getComponent(DRAWABLE);
        }
    }
}
