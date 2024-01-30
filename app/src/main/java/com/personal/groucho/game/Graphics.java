package com.personal.groucho.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.levels.Level;

import java.util.List;

public class Graphics {
    public final static int bufferWidth = 1920;
    public final static int bufferHeight = 1080;

    public Bitmap buffer;
    private final Canvas canvas;


    public Graphics() {
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(buffer);
    }

    public synchronized void render(List<GameObject> objects, Level level, Controller controller) {
        canvas.drawARGB(255,0,0,0);
        level.draw(canvas);
        drawGameObjects(objects);
        controller.draw(canvas);
    }

    private void drawGameObjects(List<GameObject> objects) {
        //TODO: use a better solution
        for (GameObject gameObject : objects) {
            Component drawComponent = gameObject.getComponent(ComponentType.Drawable);
            if (drawComponent != null){
                DrawableComponent drawable = (DrawableComponent) drawComponent;
                drawable.draw(canvas);
            }
        }

        //TODO: use a better solution
        for (GameObject gameObject : objects) {
            Component lightComponent = gameObject.getComponent(ComponentType.Light);
            if (lightComponent != null){
                LightComponent light = (LightComponent) lightComponent;
                light.draw(canvas);
            }
        }
    }

    public Bitmap getBuffer() {return buffer;}
    public Canvas getCanvas() {return canvas;}
}
