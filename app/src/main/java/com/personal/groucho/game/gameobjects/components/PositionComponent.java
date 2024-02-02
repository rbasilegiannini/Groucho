package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.cellSize;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class PositionComponent extends Component {
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

    public int getPosX() {return posX;}
    public int getPosY() {return posY;}
    public Vec2 getPosition() { return new Vec2(posX, posY);}
    public int getPositionXOnGrid() {return posX /cellSize;}
    public int getPositionYOnGrid() {return posY /cellSize;}
    public Orientation getOrientation() {return orientation;}

    public void setPosX(int newPosX) {this.posX = newPosX;}
    public void setPosY(int newPosY) {this.posY = newPosY;}
    public void setOrientation(Orientation orientation) {this.orientation = orientation;}

    public void updatePosX(int increase) {this.posX += increase;}
    public void updatePosY(int increase) {this.posY += increase;}

}
