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

enum TriggerType {
    CHANGE_LEVEL,
    TALK
}

public abstract class Level {

    protected final GameWorld gameWorld;
    protected final List<GameObject> gameObjects = new ArrayList<>();
    protected GameGrid grid;
    protected final Rect surface;
    protected final Paint floorPaint;
    private final int widthGrid;
    private final int heightGrid;

    protected Level(GameWorld gameWorld, int levelDimX, int levelDimY) {
        this.gameWorld = gameWorld;
        widthGrid = levelDimX/cellSize;
        heightGrid = levelDimY/cellSize;
        surface = new Rect(0,0, widthGrid*cellSize, heightGrid*cellSize);
        floorPaint = new Paint();
    }

    public void init() {
        grid = new GameGrid(widthGrid, heightGrid, gameWorld);
        gameObjects.clear();
        gameWorld.setGameGrid(grid);
        makeBorders();
    }

    public void draw(Canvas canvas){
        canvas.drawRect(surface, floorPaint);
    }

    public abstract void handleTrigger(GameObject trigger);

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

    private void makeBorders() {
        // Upper border
        gameObjects.addAll(GameObjectFactory.
                makeHorBorder(surface.width() / 2,
                        -cellSize,
                        surface.width(),
                        gameWorld
                ));

        // Bottom border
        gameObjects.addAll(GameObjectFactory.
                makeHorBorder(surface.width()/2,
                        surface.height(),
                        surface.width() + cellSize,
                        gameWorld
                ));

        // Left border
        gameObjects.add(GameObjectFactory.makeVerBorder(
                cellSize/4,
                (int) ((surface.height()/2)-(1.75f * cellSize)),
                surface.height()+(1.5f*cellSize),
                gameWorld));

        // Right border
        gameObjects.add(GameObjectFactory.makeVerBorder(
                (int) (surface.width() + (0.74f)*cellSize),
                (int) ((surface.height()/2)-(1.75f * cellSize)),
                surface.height()+(1.5f*cellSize),
                gameWorld));
    }
}
