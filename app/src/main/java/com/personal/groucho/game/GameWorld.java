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
    final static int bufferWidth = 400, bufferHeight = 600;    // actual pixels
    Bitmap buffer;
    private Canvas canvas;

    final Box physicalSize, screenSize, currentView;
    final Activity activity;

    List<GameObject> objects;

    public GameWorld(Box physicalSize, Box screenSize, Activity activity) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.currentView = physicalSize;
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);

        //

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
    }

    public float toMetersX(float x) { return currentView.xmin + x * (currentView.width/screenSize.width); }
    public float toMetersY(float y) { return currentView.ymin + y * (currentView.height/screenSize.height); }

    public float toPixelsX(float x) { return (x-currentView.xmin)/currentView.width*bufferWidth; }
    public float toPixelsY(float y) { return (y-currentView.ymin)/currentView.height*bufferHeight; }

    public float toPixelsXLength(float x)
    {
        return x/currentView.width*bufferWidth;
    }
    public float toPixelsYLength(float y)
    {
        return y/currentView.height*bufferHeight;
    }
}
