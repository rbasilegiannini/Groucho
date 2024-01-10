package com.personal.groucho.game;

public class GameObject extends Entity{
    protected String name;

    GameObject() {this.name = "";}
    GameObject(String name) { this.name = name;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
