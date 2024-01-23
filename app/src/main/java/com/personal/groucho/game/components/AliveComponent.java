package com.personal.groucho.game.components;

import android.util.Log;

import com.personal.groucho.game.GameObject;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.Status;

public class AliveComponent extends Component{
    private int currentHealth;
    private Status currentStatus;

    public AliveComponent (int health) {
        currentHealth = health;
        currentStatus = Status.ALIVE;
    }

    @Override
    public ComponentType type() {return ComponentType.Alive;}

    public void damage(int power) {
        currentHealth -= power;
        Log.i("Alive", ((GameObject)owner).getName()+" current health: " + currentHealth);
        if (currentHealth < 0)
            die();
    }

    private void die() {
        currentStatus = Status.DEAD;

        Component spriteComponent = owner.getComponent(ComponentType.Drawable);
        if (spriteComponent != null) {
            SpriteDrawableComponent sprite = (SpriteDrawableComponent) spriteComponent;
            sprite.setDeathSpritesheet();
        }
    }

    public Status getCurrentStatus() {return currentStatus;}
}
