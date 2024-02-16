package com.personal.groucho.game.levels;

import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Bitmap;
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

    public void draw(Canvas canvas){canvas.drawRect(surface, floorPaint);}

    public abstract void handleTrigger(GameObject trigger);

    private void makeBorders() {
        // Upper border
        List<GameObject> borders = new ArrayList<>(GameObjectFactory.
                makeHorizontalBorder(surface.width() / 2,
                        -cellSize / 4,
                        surface.width() + cellSize,
                        gameWorld
                ));

        // Bottom border
        borders.addAll(GameObjectFactory.
                makeHorizontalBorder(surface.width()/2,
                        surface.height()-cellSize/4,
                        surface.height() + cellSize,
                        gameWorld
                ));

        // Left border
        borders.add(GameObjectFactory.makeVerticalBorder(
                cellSize/4, (int) (surface.height()/2 - 1.5f*cellSize),
                surface.height(), gameWorld));

        // Right border
        borders.add(GameObjectFactory.makeVerticalBorder(
                (int) (surface.width() + (0.75f)*cellSize),
                (int) (surface.height()/2 - 1.5f*cellSize), surface.height(), gameWorld));


        for (GameObject go : borders) {
            gameWorld.addGameObject(go);
        }
    }
}
