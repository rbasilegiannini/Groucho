package com.personal.groucho.game.levels;

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
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.components.AliveComponent;

public class FirstLevel extends Level{
    private final Paint floorPaint;
    private final GameWorld gameWorld;
    private final Bitmap floor;
    private final Rect surface;

    public FirstLevel(GameWorld gw) {
        gameWorld = gw;
        World world = gw.getWorld();
        floor = Textures.firstLevelFloor;
        surface = new Rect(0,0, bufferWidth, bufferHeight);

        // Set floor
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint = new Paint();
        floorPaint.setShader(bs);
        Matrix m = new Matrix();
        m.postTranslate(surface.width(),surface.height());
        floorPaint.getShader().setLocalMatrix(m);

        // Set furniture
        gameWorld.addGameObject(GameObjectFactory.
                makeWall(bufferWidth, bufferHeight/2, 100, bufferHeight, gameWorld)
        );
        gameWorld.addGameObject(GameObjectFactory.
                makeFurniture(
                        bufferWidth/2,
                        (int)(0.50*bufferHeight)+500,
                        250, 150,
                        gameWorld,
                        Textures.table)
        );

        gameWorld.addGameObject(GameObjectFactory.
                makeWall(bufferWidth/2, 0, bufferWidth, 100, gameWorld)
        );

        // Set enemies
        gameWorld.addGameObject(GameObjectFactory.
                makeEnemy(
                        100,(int)(0.75*bufferHeight)+100,
                        skeletonHealth,
                        Spritesheets.skeleton_idle,
                        Spritesheets.skeleton_death,
                        gameWorld)
        );

        // Set health
        gameWorld.addGameObject(GameObjectFactory.
                makeHealth(bufferWidth/2, 300, gameWorld));
        gameWorld.addGameObject(GameObjectFactory.
                makeHealth(bufferWidth/2 + 100, 300, gameWorld));
        gameWorld.addGameObject(GameObjectFactory.
                makeHealth(1000, 1000, gameWorld));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(surface, floorPaint);
    }
}
