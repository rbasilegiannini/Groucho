package com.personal.groucho.game.levels;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

import com.personal.groucho.game.GameObjectFactory;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.animation.Spritesheets;
import com.personal.groucho.game.assets.Textures;

public class FirstLevel extends Level{
    private Paint floorPaint;
    private GameWorld gameWorld;
    private Bitmap floor;
    private Rect surface;

    public FirstLevel(GameWorld gw) {
        gameWorld = gw;
        floor = Textures.firstLevelFloor;
        surface = new Rect(0,0, 2000, 2000);

        // Set floor
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint = new Paint();
        floorPaint.setShader(bs);
        Matrix m = new Matrix();
        m.postTranslate(surface.width(),surface.height());
        floorPaint.getShader().setLocalMatrix(m);

        // Set enemies
        gameWorld.addGameObject(GameObjectFactory.
                makeEnemy(100,100, Spritesheets.skeleton_idle, gw.world)
        );
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(surface, floorPaint);
    }
}
