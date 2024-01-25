package com.personal.groucho.game.gameobjects;

public class GameObject extends Entity{
    protected String name;
    public Role role;

    GameObject() {
        this.name = "";
        this.role = Role.NEUTRAL;
    }

    GameObject(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
