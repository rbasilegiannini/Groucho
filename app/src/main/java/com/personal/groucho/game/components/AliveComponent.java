package com.personal.groucho.game.components;

import android.util.Log;

import com.personal.groucho.game.GameObject;
import com.personal.groucho.game.Status;

public class AliveComponent extends Component{
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
    public ComponentType type() {return ComponentType.Alive;}

    public void damage(int power) {
        currentHealth -= power;

        if (currentHealth <= 0)
            die();

        if (sprite == null) {
            Component spriteComponent = owner.getComponent(ComponentType.Drawable);
            if (spriteComponent != null) {
                sprite = (SpriteDrawableComponent) spriteComponent;
                sprite.updateColorFilter(currentHealth, maxHealth);
            }
        }
        else
            sprite.updateColorFilter(currentHealth, maxHealth);
    }

    private void die() {
        currentStatus = Status.DEAD;

        if (sprite != null)
            sprite.setDeathSpritesheet();
    }

    public Status getCurrentStatus() {return currentStatus;}
}
