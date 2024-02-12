package com.personal.groucho.game;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

public class Player {
    private final GameObject playerGO;
    private int playerPosX, playerPosY;
    private final Matrix matrix;
    private boolean isPlayerVisible = false;

    public Player(GameObject playerGO, int playerPosX, int playerPosY) {
        this.playerGO = playerGO;
        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;
        matrix = new Matrix();
    }

    public void update(Canvas canvas, Controller controller){
        updatePlayerState();
        updateCamera(canvas, controller);
    }

    private void updatePlayerState() {
        Component ctrlComponent = playerGO.getComponent(ComponentType.CONTROLLABLE);
        if (ctrlComponent != null) {
            ControllableComponent controllable = (ControllableComponent) ctrlComponent;
            controllable.updatePlayerState();
        }
    }

    private void updateCamera(Canvas canvas, Controller controller) {
        Component component = playerGO.getComponent(ComponentType.POSITION);
        if (component != null) {
            PositionComponent position = (PositionComponent) component;
            float cameraX = playerPosX - position.getPosX();
            float cameraY = playerPosY - position.getPosY();
            moveCamera(canvas, cameraX, cameraY);
            controller.updateControllerPosition(-cameraX, -cameraY);

            playerPosX = position.getPosX();
            playerPosY =  position.getPosY();
        }
    }

    private void moveCamera(Canvas canvas, float cameraX, float cameraY) {
        matrix.reset();
        matrix.postTranslate(cameraX, cameraY);
        canvas.concat(matrix);
    }

    public void setPlayerVisibility(boolean visibility) {isPlayerVisible = visibility;}
    public boolean getPlayerVisibility() {return isPlayerVisible;}
    public Vec2 getPosition() {return new Vec2(playerPosX, playerPosY);}
    public GameObject getGameObject() {return playerGO;}

}
