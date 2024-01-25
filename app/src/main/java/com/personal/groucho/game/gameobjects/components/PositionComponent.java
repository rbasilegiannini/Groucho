package com.personal.groucho.game.gameobjects.components;

public class PositionComponent extends Component{
    private int posX;
    private int posY;

    public PositionComponent(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public ComponentType type() {
        return ComponentType.Position;
    }

    public int getPosX() {return this.posX;}
    public int getPosY() {return this.posY;}

    public void setPosX(int newPosX) {this.posX = newPosX;}
    public void setPosY(int newPosY) {this.posY = newPosY;}

    public void updatePosX(int increase) {this.posX += increase;}
    public void updatePosY(int increase) {this.posY += increase;}
}