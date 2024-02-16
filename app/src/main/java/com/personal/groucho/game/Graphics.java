package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.Comparator;

class DrawableComparator implements Comparator<DrawableComponent> {
    private static DrawableComparator instance = null;
    private GameObject go1, go2;

    public static DrawableComparator getInstance() {
        if (instance == null) {
            instance = new DrawableComparator();
        }
        return instance;
    }

    @Override
    public int compare(DrawableComponent obj1, DrawableComponent obj2) {
        go1 = (GameObject) obj1.getOwner();
        go2 = (GameObject) obj2.getOwner();

        if (go1.role == Role.ENEMY) {
            if (((AliveComponent)(go1.getComponent(ALIVE))).currentStatus == DEAD){
                return -1;
            }
        }

        if (go2.role == Role.ENEMY) {
            if (((AliveComponent)(go2.getComponent(ALIVE))).currentStatus == DEAD){
                return 1;
            }
        }

        return Integer.compare(
                ((PositionComponent)go1.getComponent(POSITION)).posY,
                ((PositionComponent)go2.getComponent(POSITION)).posY);
    }
}

public class Graphics {
    public final static int bufferWidth = 1920;
    public final static int bufferHeight = 1080;

    protected Bitmap buffer;
    protected final Canvas canvas;
    private final GameWorld gameWorld;

    public Graphics(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(buffer);
    }

    public synchronized void render() {
        canvas.drawARGB(255,0,0,0);
        gameWorld.currentLevel.draw(canvas);
        drawGameObjects();
        if (!gameWorld.isGameOver()) {
            gameWorld.controller.draw(canvas);
        }
    }

    private void drawGameObjects() {
        gameWorld.drawComponents.sort(DrawableComparator.getInstance());

        for (DrawableComponent drawComponent : gameWorld.drawComponents) {
            drawComponent.draw(canvas);
        }

//        for (LightComponent lightComponent : gameWorld.lightComponents) {
//            lightComponent.draw(canvas);
//        }
    }
}
