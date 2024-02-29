package com.personal.groucho.game.gameobjects;

import android.util.SparseArray;

public class Entity {
    protected final SparseArray<Component> components = new SparseArray<>();
    public Role role;

    public Component getComponent(ComponentType type) {
        return components.get(type.hashCode());
    }

    public void addComponent(Component component) {
        component.setOwner(this);
        components.put(component.type().hashCode(), component);
    }

    public void removeComponent(ComponentType type) {
        Component component = components.get(type.hashCode());
        if (component != null){
            component.delete();
            components.remove(type.hashCode());
        }
    }

    public void delete() {
        for (int i = 0; i < components.size(); i++){
            components.valueAt(i).delete();
        }
        components.clear();
    }
}
