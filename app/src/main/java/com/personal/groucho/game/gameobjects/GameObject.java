package com.personal.groucho.game.gameobjects;

public class GameObject extends Entity implements Resettable {
    public String name;

    public GameObject() {
        this.name = "";
        this.role = Role.NEUTRAL;
    }

    GameObject(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public void init(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    @Override
    public void reset() {
        this.name = "";
        this.role = Role.NEUTRAL;

        for (int i = 0; i < components.size(); i++) {
            components.valueAt(i).delete();
        }
        components.clear();
    }
}
