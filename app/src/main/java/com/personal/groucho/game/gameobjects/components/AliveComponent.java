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
    private int currentHealth;
    public Status currentStatus;
    private SpriteDrawableComponent spriteComp = null;
    private CharacterComponent character = null;
    private final GameWorld gameWorld;

    public AliveComponent(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        currentStatus = Status.ALIVE;
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
        spriteComp.setCurrentSpritesheet(character.properties.sheetDeath);
        spriteComp.setAnim(0);
        gameWorld.handleDeath((GameObject) getOwner());
    }

    private void updateSprite() {
        initComponents();
        spriteComp.updateColorFilter(currentHealth, maxHealth);
    }

    private void initComponents(){
        if (character == null) {
            character = (CharacterComponent) owner.getComponent(CHARACTER);
            maxHealth = character.properties.health;
            currentHealth = character.properties.health;
        }
        if (spriteComp == null) {
            spriteComp = (SpriteDrawableComponent) owner.getComponent(DRAWABLE);
        }
    }
}
