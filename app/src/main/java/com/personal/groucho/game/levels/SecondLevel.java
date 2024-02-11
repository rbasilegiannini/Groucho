package com.personal.groucho.game.levels;

import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.System.characterDimensionsY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;

public class SecondLevel extends Level {

    public SecondLevel(GameWorld gameWorld) {
        super(gameWorld, 2000, 2000);
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

        // Set furniture
        gameWorld.addGameObject(GameObjectFactory.
                makeWall((9 * cellSize), (2 * cellSize) - cellSize / 2,
                        cellSize, 4 * cellSize - characterScaleFactor * characterDimensionsY,
                        gameWorld)
        );

    }

    @Override
    public void handleTrigger(GameObject trigger) {

    }
}
