package com.personal.groucho.game;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

import com.personal.groucho.R;
import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.components.Component;
import com.personal.groucho.game.components.ComponentType;
import com.personal.groucho.game.components.ControllableComponent;
import com.personal.groucho.game.components.DrawableComponent;
import com.personal.groucho.game.levels.FirstLevel;
import com.personal.groucho.game.levels.Level;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    final static int bufferWidth = 1920, bufferHeight = 1080;    // actual pixels
    Bitmap buffer;
    private final Canvas canvas;

    final Box physicalSize, screenSize, currentView;
    final Activity activity;
    private final Controller controller;
    List<GameObject> objects;
    private TouchHandler touchHandler;

    private Level currentLevel;

    public GameWorld(Box physicalSize, Box screenSize, Activity activity) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;
        this.currentView = physicalSize;
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);

        this.controller = new Controller((float) 200, (float) bufferHeight /2, this);

        this.objects = new ArrayList<>();
        this.canvas = new Canvas(buffer);
        this.currentLevel = new FirstLevel(this);
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
        updatePlayerState();
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
        for (GameObject gameObject : objects) {
            Component component = gameObject.getComponent(ComponentType.Controllable);
            if (component != null) {
                ControllableComponent controllable = (ControllableComponent) component;
                controllable.updatePlayerState();
            }
        }
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public Controller getController() { return controller; }

    public float fromScreenToBufferX(float x) { return x/screenSize.width*bufferWidth; }
    public float fromScreenToBufferY(float y) { return y/screenSize.height*bufferHeight; }
}
