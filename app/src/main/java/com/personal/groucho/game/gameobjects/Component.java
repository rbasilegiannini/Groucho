package com.personal.groucho.game.gameobjects;

public abstract class Component {
    protected Entity owner;

    public abstract ComponentType type();
    public void setOwner(Entity owner) {this.owner = owner;}
    public void delete() {}
}
