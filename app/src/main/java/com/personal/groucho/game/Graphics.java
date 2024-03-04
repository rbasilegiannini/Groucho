package com.personal.groucho.game;

import static com.personal.groucho.game.constants.System.fpsCounter;
import static com.personal.groucho.game.constants.System.memoryUsage;
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Role.ENEMY;
import static com.personal.groucho.game.gameobjects.Role.FLOOR;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.personal.groucho.game.gameobjects.Entity;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.Comparator;

class DrawableComparator implements Comparator<DrawableComponent> {
    private static DrawableComparator instance = null;

    public static DrawableComparator getInstance() {
        if (instance == null) {
            instance = new DrawableComparator();
        }
        return instance;
    }

    @Override
    public int compare(DrawableComponent obj1, DrawableComponent obj2) {
        Entity e1 = obj1.getOwner();
        Entity e2 = obj2.getOwner();

        if (e1.role == FLOOR) return -1;
        if (e2.role == FLOOR) return 1;

        if (e1.role == ENEMY || e1.role == PLAYER) {
            if (((AliveComponent)(e1.getComponent(ALIVE))).currentStatus == DEAD){
                return -1;
            }
        }

        if (e2.role == ENEMY || e2.role == PLAYER) {
            if (((AliveComponent)(e2.getComponent(ALIVE))).currentStatus == DEAD){
                return 1;
            }
        }

        return Integer.compare(
                ((PositionComponent) e1.getComponent(POSITION)).posY,
                ((PositionComponent) e2.getComponent(POSITION)).posY);
    }
}

public class Graphics {
    private static Graphics instance = null;

    public final static int bufferWidth = 1920;
    public final static int bufferHeight = 1080;

    public Bitmap buffer;
    protected Canvas canvas;
    private final GameWorld gameWorld;

    private Graphics(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(buffer);
    }

    public static Graphics getInstance(GameWorld gameWorld){
        if (instance == null) {
            instance = new Graphics(gameWorld);
        }
        return instance;
    }

    public synchronized void render() {
        canvas.drawARGB(255,0,0,0);
        gameWorld.currentLevel.draw(canvas);
        drawGameObjects();

        if (!gameWorld.gameOver) {
            gameWorld.controller.draw(canvas);
        }

        if (fpsCounter || memoryUsage){
            Logger.getInstance().draw(canvas);
        }
    }

    private void drawGameObjects() {
        ComponentHandler.getInstance().drawComps.sort(DrawableComparator.getInstance());

        for (DrawableComponent drawComp : ComponentHandler.getInstance().drawComps) {
            drawComp.draw(canvas);
        }

        for (LightComponent lightComponent : ComponentHandler.getInstance().lightComps) {
            lightComponent.draw(canvas);
        }
    }

    int fadeOut = 0;
    public void fadeOut() {
        fadeOut += 5;
        render();
        canvas.drawARGB(Math.min(fadeOut, 255),0,0,0);

        if (fadeOut >= 255){
            gameWorld.gameOver = false;
            MenuHandler.handleGameOverMenu(gameWorld);
            fadeOut = 0;
        }
    }

    public void reset() {
        this.canvas = new Canvas(buffer);
    }

    protected void finalize(){
        try {
            super.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        if (buffer != null) {
            buffer.recycle();
            buffer = null;
        }
        instance = null;
    }
}
