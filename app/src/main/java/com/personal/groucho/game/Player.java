package com.personal.groucho.game;

import static com.personal.groucho.game.constants.System.fpsCounter;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Canvas;

import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

public class Player {
    protected final GameObject gameObject;
    private final PositionComponent posComponent;
    private final ControllableComponent ctrlComponent;
    protected int posX, posY;
    private float cameraX, cameraY;
    protected boolean isPlayerVisible = false;

    public Player(GameObject gameObject, int posX, int posY) {
        this.gameObject = gameObject;
        posComponent = (PositionComponent) gameObject.getComponent(POSITION);
        ctrlComponent = (ControllableComponent) gameObject.getComponent(CONTROLLABLE);
        this.posX = posX;
        this.posY = posY;
        this.cameraX = 0;
        this.cameraY = 0;

        if (fpsCounter) {
            FPSCounter.getInstance().posX = posX - (float) Graphics.bufferWidth /2;
            FPSCounter.getInstance().posY = posY - (float) Graphics.bufferHeight /2+50;
        }
    }

    public void update(Canvas canvas, Controller controller){
        ctrlComponent.updatePlayerState();
        if (posComponent.hasChangedPosition()) {
            updateCamera(canvas, controller);
        }

        if (fpsCounter) {
            FPSCounter.getInstance().posX = posX - (float) Graphics.bufferWidth /2;
            FPSCounter.getInstance().posY = posY - (float) Graphics.bufferHeight /2+50;
        }
    }

    private void updateCamera(Canvas canvas, Controller controller) {
        cameraX = posX - posComponent.posX;
        cameraY = posY - posComponent.posY;

        canvas.translate(cameraX, cameraY);
        controller.updateControllerPosition(-cameraX, -cameraY);

        posX = posComponent.posX;
        posY =  posComponent.posY;
    }

    public void setPlayerVisibility(boolean visibility) {isPlayerVisible = visibility;}
    public void setPos(int posX, int posY) {
        PhysicsComponent phyComponent = (PhysicsComponent) gameObject.getComponent(PHYSICS);
        phyComponent.setPos(posX, posY);
    }
}
