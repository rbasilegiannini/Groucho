package com.personal.groucho.game.gameobjects;

import com.personal.groucho.game.GameWorld;

public class GameObject extends Entity{
    protected String name;
    public Role role;

    GameObject(GameWorld gameWorld) {
        this.name = "";
        this.role = Role.NEUTRAL;
    }

    GameObject(String name, Role role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Override
    public void delete(){
        super.delete();
    }
}
