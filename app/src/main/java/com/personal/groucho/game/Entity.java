package com.personal.groucho.game;

import java.util.Map;

public class Entity {
    private Map<ComponentType, Component> components;

    public void addComponent(Component component) {
        component.setOwner(this);
        components.put(component.type(), component);
    }

    public Component getComponent(ComponentType type) {
        return components.get(type);
    }
}
