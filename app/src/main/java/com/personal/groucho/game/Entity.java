package com.personal.groucho.game;

import com.personal.groucho.game.components.Component;
import com.personal.groucho.game.components.ComponentType;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private final Map<ComponentType, Component> components;

    public Entity() {
        components = new HashMap<>();
    }

    public void addComponent(Component component) {
        component.setOwner(this);
        components.put(component.type(), component);
    }

    public Component getComponent(ComponentType type) {
        return components.get(type);
    }
}