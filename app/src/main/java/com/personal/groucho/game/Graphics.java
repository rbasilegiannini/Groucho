package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.Status;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.Comparator;

class DrawableComparator implements Comparator<DrawableComponent> {
    @Override
    public int compare(DrawableComponent obj1, DrawableComponent obj2) {
        if (((GameObject)obj1.getOwner()).role == Role.ENEMY) {
            if (((AliveComponent)(obj1.getOwner().getComponent(ALIVE))).getCurrentStatus() == Status.DEAD){
                return -1;
            }
        }

        if (((GameObject)obj2.getOwner()).role == Role.ENEMY) {
            if (((AliveComponent)(obj2.getOwner().getComponent(ALIVE))).getCurrentStatus() == Status.DEAD){
                return 1;
            }
        }

        return Integer.compare(
                ((PositionComponent)obj1.getOwner().getComponent(POSITION)).getPosY(),
                ((PositionComponent)obj2.getOwner().getComponent(POSITION)).getPosY());
    }
}

public class Graphics {
    public final static int bufferWidth = 1920;
    public final static int bufferHeight = 1080;

    public Bitmap buffer;
    private final Canvas canvas;
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
        gameWorld.drawComponents.sort(new DrawableComparator());

        for (DrawableComponent drawComponent : gameWorld.drawComponents) {
            drawComponent.draw(canvas);
        }

        for (LightComponent lightComponent : gameWorld.lightComponents) {
            lightComponent.draw(canvas);
        }
    }

    public Bitmap getBuffer() {return buffer;}
    public Canvas getCanvas() {return canvas;}
}
