package com.personal.groucho.game.gameobjects;

import com.personal.groucho.game.GameWorld;

public class GameObject extends Entity{
    public String name;
    public Role role;

    public GameObject(GameWorld gameWorld) {
        this.name = "";
        this.role = Role.NEUTRAL;
    }

    GameObject(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public void setName(String name) {this.name = name;}

    @Override
    public void delete(){
        super.delete();
    }
}
