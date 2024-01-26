package com.personal.groucho.game;

import android.graphics.Canvas;
import android.graphics.Matrix;

import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.states.Walking;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.Component;
import com.personal.groucho.game.gameobjects.components.ComponentType;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

public class Player {
    private final GameObject playerGO;
    private int playerPosX, playerPosY;

    public Player(GameObject playerGO, int playerPosX, int playerPosY) {
        this.playerGO = playerGO;
        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;
    }

    public void update(Canvas canvas, Controller controller){
        updatePlayerState();
        updateCamera(canvas, controller);
    }

    private void updatePlayerState() {
        Component ctrlComponent = playerGO.getComponent(ComponentType.Controllable);
        if (ctrlComponent != null) {
            ControllableComponent controllable = (ControllableComponent) ctrlComponent;
            controllable.updatePlayerState();
        }
    }

    private void updateCamera(Canvas canvas, Controller controller) {
        Component component = playerGO.getComponent(ComponentType.Position);
        if (component != null) {
            if (controller.getPlayerState().getClass().equals(Walking.class)) {
                PositionComponent position = (PositionComponent) component;
                float cameraX = playerPosX - position.getPosX();
                float cameraY = playerPosY - position.getPosY();
                moveCamera(canvas, cameraX, cameraY);
                controller.updateControllerPosition(-cameraX, -cameraY);

                playerPosX = position.getPosX();
                playerPosY =  position.getPosY();
            }
        }
    }

    private void moveCamera(Canvas canvas, float cameraX, float cameraY) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(cameraX, cameraY);
        canvas.concat(matrix);
    }
}
