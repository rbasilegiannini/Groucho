package com.personal.groucho.game.levels;

import static com.personal.groucho.game.Constants.cellSize;
import static com.personal.groucho.game.Constants.skeletonHealth;
import static com.personal.groucho.game.Graphics.bufferHeight;
import static com.personal.groucho.game.Graphics.bufferWidth;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.assets.Textures;

public class FirstLevel extends Level{
    private final Paint floorPaint;
    private final GameWorld gameWorld;
    private final Bitmap floor;
    private final Rect surface;
    private final GameGrid grid;

    public FirstLevel(GameWorld gw) {
        gameWorld = gw;
        World world = gw.getWorld();
        floor = Textures.firstLevelFloor;
        surface = new Rect(0,0, bufferWidth, bufferHeight);
        grid = new GameGrid(2000/cellSize, 2000/cellSize, cellSize);
        gameWorld.setGameGrid(grid);

        // Set floor
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint = new Paint();
        floorPaint.setShader(bs);
        Matrix m = new Matrix();
        m.postTranslate(surface.width(),surface.height());
        floorPaint.getShader().setLocalMatrix(m);

        // Set furniture
        gameWorld.addGameObject(GameObjectFactory.
                makeWall(bufferWidth-300, bufferHeight/2, 100, bufferHeight-500, gameWorld)
        );
        gameWorld.addGameObject(GameObjectFactory.
                makeFurniture(
                        bufferWidth/2,
                        (int)(0.50*bufferHeight)+500,
                        250, 150,
                        gameWorld,
                        Textures.table)
        );

//        gameWorld.addGameObject(GameObjectFactory.
//                makeWall(bufferWidth/2, 0, bufferWidth, 100, gameWorld)
//        );

        // Set enemies
        gameWorld.addGameObject(GameObjectFactory.
                makeEnemy(
                        124,600,
                        skeletonHealth,
                        Spritesheets.skeletonIdle,
                        Spritesheets.skeletonDeath,
                        gameWorld, grid)
        );

        // Set health
//        gameWorld.addGameObject(GameObjectFactory.
//                makeHealth(-253, 556, gameWorld));
//        gameWorld.addGameObject(GameObjectFactory.
//                makeHealth(100, 410, gameWorld));
//        gameWorld.addGameObject(GameObjectFactory.
//                makeHealth(453, 556, gameWorld));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(surface, floorPaint);
    }
}
