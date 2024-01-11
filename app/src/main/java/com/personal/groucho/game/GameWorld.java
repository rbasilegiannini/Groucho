package com.personal.groucho.game;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.personal.groucho.game.components.Component;
import com.personal.groucho.game.components.ComponentType;
import com.personal.groucho.game.components.DrawableComponent;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    final static int bufferWidth = 2280, bufferHeight = 1080;    // actual pixels
    Bitmap buffer;
    private Canvas canvas;

    final Box physicalSize, screenSize, currentView;
    final Activity activity;

    private Controller controller;

    List<GameObject> objects;

    public GameWorld(Box physicalSize, Box screenSize, Activity activity) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.currentView = physicalSize;
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        //

        controller = new Controller((float) 200, (float) bufferHeight /2, this);

        this.objects = new ArrayList<>();
        this.canvas = new Canvas(buffer);
    }

    public synchronized GameObject addGameObject(GameObject obj)
    {
        objects.add(obj);
        return obj;
    }

    public synchronized void update(float elapsedTime)
    {

    }

    public synchronized void render()
    {
        canvas.drawARGB(255,0,0,0);
        for (GameObject gameObject : objects) {
            Component component = gameObject.getComponent(ComponentType.Drawable);
            if (component != null) {
                DrawableComponent drawable = (DrawableComponent) component;
                drawable.draw(canvas);
            }
        }
        controller.draw(canvas);
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public float fromScreenToBufferX(float x) { return x/screenSize.width*bufferWidth; }
    public float fromScreenToBufferY(float y) { return y/screenSize.height*bufferHeight; }
}
