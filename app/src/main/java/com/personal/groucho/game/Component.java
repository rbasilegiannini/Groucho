package com.personal.groucho.game;

abstract class Component {
    protected  Entity owner;
    public abstract ComponentType type();
    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}
