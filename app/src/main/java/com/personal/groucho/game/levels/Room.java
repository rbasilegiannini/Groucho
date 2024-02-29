package com.personal.groucho.game.levels;

import static com.personal.groucho.game.constants.Environment.brightness;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class Room {
    protected final GameWorld gameWorld;
    protected final List<GameObject> gameObjects = new ArrayList<>();
    protected final Rect surface;
    protected final Paint floorPaint;
    private final int widthGrid;
    private final int heightGrid;

    public Room(int levelDimX, int levelDimY, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        widthGrid = levelDimX/cellSize;
        heightGrid = levelDimY/cellSize;
        surface = new Rect(0,0, widthGrid*cellSize, heightGrid*cellSize);
        floorPaint = new Paint();
    }

    public void init() {
        GameGrid.getInstance().init(widthGrid, heightGrid);
        gameObjects.clear();
        makeBorders();
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(surface, floorPaint);
    }

    private void makeBorders() {
        // Upper border
        gameObjects.addAll(GameObjectFactory.
                makeHorBorder(surface.width() / 2,
                        -cellSize,
                        surface.width(),
                        gameWorld.physics.world
                ));

        // Bottom border
        gameObjects.addAll(GameObjectFactory.
                makeHorBorder(surface.width()/2,
                        surface.height(),
                        surface.width() + cellSize,
                        gameWorld.physics.world
                ));

        // Left border
        gameObjects.add(GameObjectFactory.makeVerBorder(
                cellSize/4,
                (int) ((surface.height()/2)-(1.75f * cellSize)),
                surface.height()+(1.5f*cellSize),
                gameWorld.physics.world));

        // Right border
        gameObjects.add(GameObjectFactory.makeVerBorder(
                (int) (surface.width() + (0.74f)*cellSize),
                (int) ((surface.height()/2)-(1.75f * cellSize)),
                surface.height()+(1.5f*cellSize),
                gameWorld.physics.world));
    }

    protected void setBrightness(float intensity) {
        if (intensity >= maxBrightness) {
            brightness = maxBrightness;
            gameWorld.setPlayerVisibility(true);
        }
        else {
            brightness = minBrightness;
            gameWorld.setPlayerVisibility(false);
        }
    }

    public void releaseRoom() {
        gameWorld.goHandler.changeLevel();
        GameGrid.getInstance().releasePool();
    }

    public void allocateRoom(){
        for (GameObject go : gameObjects) {
            gameWorld.goHandler.addGameObject(go);
        }
    }
}
