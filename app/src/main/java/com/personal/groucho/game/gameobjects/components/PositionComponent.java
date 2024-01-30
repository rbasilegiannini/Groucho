package com.personal.groucho.game.gameobjects.components;

import com.personal.groucho.game.controller.Orientation;

public class PositionComponent extends Component{
    private int posX;
    private int posY;
    private Orientation orientation;

    public PositionComponent(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        orientation = Orientation.UP;
    }

    @Override
    public ComponentType type() {
        return ComponentType.Position;
    }

    public int getPosX() {return this.posX;}
    public int getPosY() {return this.posY;}
    public Orientation getOrientation() {return orientation;}

    public void setPosX(int newPosX) {this.posX = newPosX;}
    public void setPosY(int newPosY) {this.posY = newPosY;}
    public void setOrientation(Orientation orientation) {this.orientation = orientation;}

    public void updatePosX(int increase) {this.posX += increase;}
    public void updatePosY(int increase) {this.posY += increase;}
}
