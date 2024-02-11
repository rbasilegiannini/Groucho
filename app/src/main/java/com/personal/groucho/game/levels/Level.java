package com.personal.groucho.game.levels;

import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;

public abstract class Level {

    protected final GameWorld gameWorld;
    protected final GameGrid grid;
    protected final Rect surface;
    protected final Paint floorPaint;

    protected Level(GameWorld gameWorld, int levelDimX, int levelDimY) {
        this.gameWorld = gameWorld;
        int widthGrid = levelDimX/cellSize;
        int heightGrid = levelDimY/cellSize;
        grid = new GameGrid(widthGrid, heightGrid, cellSize);
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
        gameWorld.addGameObject(GameObjectFactory.
                makeWall(surface.width()/2,
                        -cellSize/4,
                        surface.width() + cellSize,
                        (float)cellSize /2,
                        gameWorld
                )
        );
        // Bottom border
        gameWorld.addGameObject(GameObjectFactory.
                makeWall(surface.width()/2,
                        surface.height() + cellSize/4,
                        surface.width() + cellSize,
                        (float)cellSize/2,
                        gameWorld
                )
        );
        // Left border
        gameWorld.addGameObject(GameObjectFactory.
                makeWall(-cellSize/4,
                        surface.height()/2,
                        (float)cellSize/2,
                        surface.height(),
                        gameWorld
                )
        );
        // Right border
        gameWorld.addGameObject(GameObjectFactory.
                makeWall(surface.width()+cellSize/4,
                        surface.height()/2,
                        (float)cellSize/2,
                        surface.height(),
                        gameWorld
                )
        );
    }
}
