package com.personal.groucho.game;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import com.personal.groucho.game.components.Component;
import com.personal.groucho.game.components.ComponentType;
import com.personal.groucho.game.components.ControllableComponent;
import com.personal.groucho.game.components.DrawableComponent;
import com.personal.groucho.game.components.PositionComponent;
import com.personal.groucho.game.levels.FirstLevel;
import com.personal.groucho.game.levels.Level;
import com.personal.groucho.game.states.Walking;
import com.personal.groucho.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    final static int bufferWidth = 1920, bufferHeight = 1080;    // actual pixels
    Bitmap buffer;
    private final Canvas canvas;
    final Box physicalSize, screenSize, currentView;
    final Activity activity;
    private World world;
    private final Controller controller;
    private List<GameObject> objects;
    private GameObject player;
    private TouchHandler touchHandler;
    private Level currentLevel;
    private int currentPlayerPosX;
    private int currentPlayerPosY;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 0;

    public GameWorld(Box physicalSize, Box screenSize, Activity activity) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.currentView = physicalSize;
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);

        this.world = new World(0, 0); // No gravity
        this.controller = new Controller((float) 200, (float) bufferHeight /2, this);

        this.objects = new ArrayList<>();
        this.canvas = new Canvas(buffer);
        this.currentLevel = new FirstLevel(this);
    }

    public void setPlayer(GameObject player) {
        this.player = player;
        Component component = player.getComponent(ComponentType.Position);
        if (component != null) {
            PositionComponent position = (PositionComponent) component;
            currentPlayerPosX = position.getPosX();
            currentPlayerPosY = position.getPosY();
        }
        addGameObject(player);
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        objects.add(obj);
        return obj;
    }

    public synchronized void processInputs(){
        for (Input.TouchEvent event: touchHandler.getTouchEvents()) {
            controller.consumeTouchEvent(event);
        }
    }

    public synchronized void update(float elapsedTime) {
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        updatePlayerState();
        updateCamera();
    }

    public synchronized void render() {
        canvas.drawARGB(255,0,0,0);
        currentLevel.draw(canvas);
        for (GameObject gameObject : objects) {
            Component component = gameObject.getComponent(ComponentType.Drawable);
            if (component != null) {
                DrawableComponent drawable = (DrawableComponent) component;
                drawable.draw(canvas);
            }
        }
        controller.draw(canvas);
    }

    private void updatePlayerState() {
        Component component = player.getComponent(ComponentType.Controllable);
        if (component != null) {
            ControllableComponent controllable = (ControllableComponent) component;
            controllable.updatePlayerState();
        }
    }

    public void updateCamera() {
        Component component = player.getComponent(ComponentType.Position);
        if (component != null) {
            if (controller.getPlayerState().getClass().equals(Walking.class)) {
                PositionComponent position = (PositionComponent) component;
                float cameraX = currentPlayerPosX - position.getPosX();
                float cameraY = currentPlayerPosY - position.getPosY();
                moveCamera(cameraX, cameraY);
                moveController(cameraX, cameraY);

                currentPlayerPosX = position.getPosX();
                currentPlayerPosY =  position.getPosY();
            }
        }
    }

    private void moveCamera(float cameraX, float cameraY) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(cameraX, cameraY);
        canvas.concat(matrix);
    }

    private void moveController(float cameraX, float cameraY) {
        controller.updateControllerPosition(-cameraX, -cameraY);
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public Controller getController() { return controller; }

    public float fromScreenToBufferX(float x) { return x/screenSize.width*bufferWidth; }
    public float fromScreenToBufferY(float y) { return y/screenSize.height*bufferHeight; }
}
