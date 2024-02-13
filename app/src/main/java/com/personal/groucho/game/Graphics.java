package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.levels.Level;

import java.util.Comparator;
import java.util.List;

class GameObjectComparator implements Comparator<GameObject> {
    @Override
    public int compare(GameObject obj1, GameObject obj2) {
        return Integer.compare(
                ((PositionComponent)obj1.getComponent(POSITION)).getPosY(),
                ((PositionComponent)obj2.getComponent(POSITION)).getPosY());
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

    public synchronized void render(List<GameObject> objects, Level level, Controller controller) {
        canvas.drawARGB(255,0,0,0);
        level.draw(canvas);
        drawGameObjects(objects);
        if (!gameWorld.isGameOver()) {
            controller.draw(canvas);
        }
    }

    private void drawGameObjects(List<GameObject> objects) {
        //TODO: fix crash when enemy dies
//        objects.sort(new GameObjectComparator());

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
