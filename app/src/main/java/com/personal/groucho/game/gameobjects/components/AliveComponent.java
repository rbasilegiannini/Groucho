package com.personal.groucho.game.gameobjects.components;

import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.Status;

public class AliveComponent extends Component {
    private final int maxHealth;
    private int currentHealth;
    private Status currentStatus;
    private SpriteDrawableComponent sprite = null;

    public AliveComponent (int health) {
        maxHealth = health;
        currentHealth = health;
        currentStatus = Status.ALIVE;
    }

    @Override
    public ComponentType type() {return ComponentType.ALIVE;}

    public void damage(int power) {
        currentHealth -= power;

        if (currentHealth <= 0)
            die();

        updateSprite();
    }

    public void heal(int medicalKit) {
        if (currentHealth+medicalKit <= maxHealth)
            currentHealth += medicalKit;
        else
            currentHealth = maxHealth;
        updateSprite();
    }

    private void die() {
        currentStatus = Status.DEAD;

        if (sprite != null)
            sprite.setDeathSpritesheet();
    }

    private void updateSprite() {
        if (sprite == null) {
            Component component = owner.getComponent(ComponentType.DRAWABLE);
            if (component != null) {
                sprite = (SpriteDrawableComponent) component;
                sprite.updateColorFilter(currentHealth, maxHealth);
            }
        }
        else
            sprite.updateColorFilter(currentHealth, maxHealth);
    }

    public Status getCurrentStatus() {return currentStatus;}
}
