package com.personal.groucho.game.components;

import com.personal.groucho.game.Entity;

public abstract class Component {
    protected Entity owner;

    public abstract ComponentType type();
    public void setOwner(Entity owner) {this.owner = owner;}
}
