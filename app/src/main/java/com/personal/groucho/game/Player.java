package com.personal.groucho.game;

import static com.personal.groucho.game.constants.System.fpsCounter;
import static com.personal.groucho.game.constants.System.memoryUsage;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Canvas;

import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.states.Idle;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

public class Player {
    public GameObject gameObject;
    private PositionComponent posComp;
    private ControllableComponent ctrlComp;
    private GameWorld gameWorld;

    public int posX;
    public int posY;
    private float cameraX, cameraY;
    public boolean isPlayerVisible = false;

    public Player(){}

    public void init(GameObject go, GameWorld gameWorld) {
        this.gameObject = go;
        this.gameWorld = gameWorld;
        posComp = (PositionComponent) gameObject.getComponent(POSITION);
        ctrlComp = (ControllableComponent) gameObject.getComponent(CONTROLLABLE);
        this.posX = posComp.posX;
        this.posY = posComp.posY;
        this.cameraX = 0;
        this.cameraY = 0;

        if (fpsCounter || memoryUsage) {
            Logger.getInstance().posX = posX - (float) Graphics.bufferWidth /2;
            Logger.getInstance().posY = posY - (float) Graphics.bufferHeight /2+50;
        }
    }

    public void reset(){
        if (gameWorld.controller.isLightOn())
            gameWorld.controller.handleLightTouchDown();
        posComp = null;
        ctrlComp = null;
    }

    public void update(Canvas canvas, Controller controller){
        if (ctrlComp != null) {
            ctrlComp.updatePlayerState();
        }

        if (posComp != null) {
            if (posComp.hasChangedPos()) {
                updateCamera(canvas, controller);
            }
        }

        if (fpsCounter || memoryUsage) {
            Logger.getInstance().posX = posX - (float) Graphics.bufferWidth /2;
            Logger.getInstance().posY = posY - (float) Graphics.bufferHeight /2+50;
        }
    }

    private void updateCamera(Canvas canvas, Controller controller) {
        cameraX = posX - posComp.posX;
        cameraY = posY - posComp.posY;

        canvas.translate(cameraX, cameraY);
        controller.updateControllerPos(-cameraX, -cameraY);

        posX = posComp.posX;
        posY =  posComp.posY;
    }

    public void setPlayerVisibility(boolean visibility) {isPlayerVisible = visibility;}
    public void setPos(int posX, int posY) {
        PhysicsComponent phyComp = (PhysicsComponent) gameObject.getComponent(PHYSICS);
        phyComp.setPos(posX, posY);
    }

    public void rest() {
        gameWorld.controller.currentState = Idle.getInstance(gameWorld.controller);
        gameWorld.controller.notifyToListeners();
    }

    public void setOrientation(Orientation orientation) {
        gameWorld.controller.setOrientation(orientation);
    }
}
