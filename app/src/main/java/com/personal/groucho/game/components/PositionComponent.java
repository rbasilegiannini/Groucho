package com.personal.groucho.game.components;

public class PositionComponent extends Component{
    private float posX;
    private float posY;

    public PositionComponent(float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public ComponentType type() {
        return ComponentType.Position;
    }

    public float getPosX() {return this.posX;}
    public float getPosY() {return this.posY;}

    public void setPosX(float newPosX) {this.posX = newPosX;}
    public void setPosY(float newPosY) {this.posY = newPosY;}

}
