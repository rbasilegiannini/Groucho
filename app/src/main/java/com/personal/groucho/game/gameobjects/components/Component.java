package com.personal.groucho.game.gameobjects.components;

import com.personal.groucho.game.gameobjects.Entity;

public abstract class Component {
    protected Entity owner;

    public abstract ComponentType type();
    public void setOwner(Entity owner) {this.owner = owner;}
}
