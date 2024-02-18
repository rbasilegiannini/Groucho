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

public abstract class Level {

    protected final GameWorld gameWorld;
    protected final List<GameObject> gameObjects = new ArrayList<>();
    protected final GameGrid grid;
    protected final Rect surface;
    protected final Paint floorPaint;

    protected Level(GameWorld gameWorld, int levelDimX, int levelDimY) {
        this.gameWorld = gameWorld;
        int widthGrid = levelDimX/cellSize;
        int heightGrid = levelDimY/cellSize;
        grid = new GameGrid(widthGrid, heightGrid);
        surface = new Rect(0,0, widthGrid*cellSize, heightGrid*cellSize);
        floorPaint = new Paint();
    }

    public void init() {
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
        List<GameObject> borders = new ArrayList<>(GameObjectFactory.
                makeHorBorder(surface.width() / 2,
                        -cellSize,
                        surface.width(),
                        gameWorld
                ));

        // Bottom border
        borders.addAll(GameObjectFactory.
                makeHorBorder(surface.width()/2,
                        (int) (surface.height()+(1.5f)*cellSize),
                        surface.width() + cellSize,
                        gameWorld
                ));

        // Left border
        borders.add(GameObjectFactory.makeVerBorder(
                cellSize/4,
                (int) ((surface.height()/2)-(1.25f * cellSize)),
                surface.height()+(2.5f*cellSize),
                gameWorld));

        // Right border
        borders.add(GameObjectFactory.makeVerBorder(
                (int) (surface.width() + (0.75f)*cellSize),
                (int) ((surface.height()/2)-(1.25f * cellSize)),
                surface.height()+(2.5f*cellSize),
                gameWorld));

        for (GameObject go : borders) {
            gameWorld.addGameObject(go);
        }
    }
}
