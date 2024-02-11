package com.personal.groucho.game.gameobjects;

import com.personal.groucho.game.GameWorld;

public class GameObject extends Entity{
    private final GameWorld gameWorld;
    protected String name;
    public Role role;

    GameObject(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.name = "";
        this.role = Role.NEUTRAL;
    }

    GameObject(String name, Role role, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
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
