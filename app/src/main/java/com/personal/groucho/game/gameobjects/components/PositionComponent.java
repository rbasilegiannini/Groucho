package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class PositionComponent extends Component {
    public int originalPosX, originalPosY, posX, posY;
    public Orientation orientation;

    public PositionComponent() {
        this.originalPosX = 0;
        this.originalPosY = 0;
        this.posX = 0;
        this.posY = 0;
        orientation = UP;
    }

    public void init(int posX, int posY) {
        this.originalPosX = posX;
        this.originalPosY = posY;
        this.posX = posX;
        this.posY = posY;
        orientation = UP;
    }

    @Override
    public ComponentType type() {
        return POSITION;
    }
    public int getPosXOnGrid() {return posX /cellSize;}
    public int getPosYOnGrid() {return posY /cellSize;}

    public void setPosX(int newPosX) {this.posX = newPosX;}
    public void setPosY(int newPosY) {this.posY = newPosY;}
    public void setOrientation(Orientation orientation) {this.orientation = orientation;}

    public void updatePosX(int increase) {this.posX += increase;}
    public void updatePosY(int increase) {this.posY += increase;}

    public boolean hasChangedPos() {
        if (posX != originalPosX || posY != originalPosY) {
            originalPosX = posX;
            originalPosY = posY;
            return true;
        }
        else {
            return false;
        }
    }
}
