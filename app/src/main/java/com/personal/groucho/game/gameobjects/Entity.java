package com.personal.groucho.game.gameobjects;

import com.personal.groucho.game.gameobjects.components.Component;
import com.personal.groucho.game.gameobjects.components.ComponentType;

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

    public void removeComponent(ComponentType type) {
        Component component = components.get(type);
        if (component != null) {
            component.delete();
            components.remove(type);
        }
    }

    public Component getComponent(ComponentType type) {
        return components.get(type);
    }
}
