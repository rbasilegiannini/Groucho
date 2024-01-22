package com.personal.groucho.game;

public class GameObject extends Entity{
    protected String name;
    Role role;

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
