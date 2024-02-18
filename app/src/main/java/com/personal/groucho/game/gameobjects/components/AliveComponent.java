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
        if (character == null) {
            character = (CharacterComponent) owner.getComponent(CHARACTER);
            maxHealth = character.properties.health;
            currentHealth = character.properties.health;
        }

        currentHealth -= power;

        if (currentHealth <= 0) {
            currentHealth = 0;
            die();
        }
        updateSprite();
    }

    public void heal(int medicalKit) {
        if (character == null) {
            character = (CharacterComponent) owner.getComponent(CHARACTER);
            maxHealth = character.properties.health;
            currentHealth = character.properties.health;
        }

        if (currentHealth+medicalKit <= maxHealth)
            currentHealth += medicalKit;
        else
            currentHealth = maxHealth;
        updateSprite();
    }

    private void die() {
        if (character == null) {
            character = (CharacterComponent) owner.getComponent(CHARACTER);
        }

        currentStatus = DEAD;

        if (sprite == null) {
            Component component = owner.getComponent(DRAWABLE);
            if (component != null) {
                sprite = (SpriteDrawableComponent) component;
                sprite.setCurrentSpritesheet(character.properties.sheetDeath);
                sprite.setAnim(0);
            }
        }
        else {
            sprite.setCurrentSpritesheet(character.properties.sheetDeath);
            sprite.setAnim(0);
        }
    }

    private void updateSprite() {
        if (sprite == null) {
            Component component = owner.getComponent(DRAWABLE);
            if (component != null) {
                sprite = (SpriteDrawableComponent) component;
                sprite.updateColorFilter(currentHealth, maxHealth);
            }
        }
        else {
            sprite.updateColorFilter(currentHealth, maxHealth);
        }
    }
}
