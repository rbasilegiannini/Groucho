package com.personal.groucho.game.gameobjects;

public class Component implements Resettable {
    protected Entity owner;

    public Component(){}

    public ComponentType type() {
        return null;
    }

    public void setOwner(Entity owner) {this.owner = owner;}
    public Entity getOwner(){return owner;}
    public void delete() {}
    public void reset() {
        owner = null;
    }
}
