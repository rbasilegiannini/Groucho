package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

public class Player {
    private final GameObject playerGO;
    private final PositionComponent posComponent;
    private final ControllableComponent ctrlComponent;
    private int playerPosX, playerPosY;
    private final Matrix matrix;
    private boolean isPlayerVisible = false;

    public Player(GameObject playerGO, int playerPosX, int playerPosY) {
        this.playerGO = playerGO;
        posComponent = (PositionComponent) playerGO.getComponent(POSITION);
        ctrlComponent = (ControllableComponent) playerGO.getComponent(CONTROLLABLE);
        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;
        matrix = new Matrix();
    }

    public void update(Canvas canvas, Controller controller){
        ctrlComponent.updatePlayerState();
        updateCamera(canvas, controller);
    }

    private void updateCamera(Canvas canvas, Controller controller) {
        float cameraX = playerPosX - posComponent.getPosX();
        float cameraY = playerPosY - posComponent.getPosY();

        moveCamera(canvas, cameraX, cameraY);
        controller.updateControllerPosition(-cameraX, -cameraY);

        playerPosX = posComponent.getPosX();
        playerPosY =  posComponent.getPosY();
    }

    private void moveCamera(Canvas canvas, float cameraX, float cameraY) {
        matrix.reset();
        matrix.postTranslate(cameraX, cameraY);
        canvas.concat(matrix);
    }

    public void setPlayerVisibility(boolean visibility) {isPlayerVisible = visibility;}
    public boolean getPlayerVisibility() {return isPlayerVisible;}
    public Vec2 getPos() {return new Vec2(playerPosX, playerPosY);}
    public GameObject getGameObject() {return playerGO;}
}
