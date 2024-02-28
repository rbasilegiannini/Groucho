package com.personal.groucho.game.levels;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObject;

import java.util.Objects;

public class Hallway extends Level {

    public Hallway(GameWorld gameWorld) {
        super(gameWorld, 2000, 1000);
        Bitmap floor = Textures.firstLevelFloor;

        // Set floor
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
        Matrix m = new Matrix();
        m.postTranslate(surface.width(), surface.height());
        floorPaint.getShader().setLocalMatrix(m);
    }

    @Override
    public void init() {
        super.init();

//        gameObjects.add(GameObjectFactory.makeTrigger("changelevel", 1200, 800, gameWorld));
        for (GameObject go : gameObjects) {
            gameWorld.goHandler.addGameObject(go);
        }
    }
}
