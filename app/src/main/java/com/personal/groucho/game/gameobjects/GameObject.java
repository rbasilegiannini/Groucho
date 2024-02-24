package com.personal.groucho.game.gameobjects;

public class GameObject extends Entity{
    public String name;
    public Role role;

    public GameObject() {
        this.name = "";
        this.role = Role.NEUTRAL;
    }

    GameObject(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public void setName(String name) {this.name = name;}

    @Override
    public void init(Entity object) {
        super.init(object);
        name = ((GameObject)object).name;
        role = ((GameObject)object).role;
    }
}
