package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.CHARACTER;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.Status;

public class AliveComponent extends Component {
    private int maxHealth;
    private int currentHealth;
    public Status currentStatus;
    private SpriteDrawableComponent sprite = null;
    private CharacterComponent character = null;

    public AliveComponent() {
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
        sprite.setCurrentSpritesheet(character.properties.sheetDeath);
        sprite.setAnim(0);
    }

    private void updateSprite() {
        initComponents();
        sprite.updateColorFilter(currentHealth, maxHealth);
    }

    private void initComponents(){
        if (character == null) {
            character = (CharacterComponent) owner.getComponent(CHARACTER);
            maxHealth = character.properties.health;
            currentHealth = character.properties.health;
        }
        if (sprite == null) {
            sprite = (SpriteDrawableComponent) owner.getComponent(DRAWABLE);
        }
    }
}
