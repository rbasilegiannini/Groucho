package com.personal.groucho.game.levels;

import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.CharacterProperties.skeletonHealth;
import static com.personal.groucho.game.Graphics.bufferHeight;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.constants.System.characterDimensionsY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;

import com.personal.groucho.game.AI.states.StateName;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.assets.Textures;

import java.util.Objects;

public class FirstLevel extends Level{

    public FirstLevel(GameWorld gameWorld) {
        super(gameWorld, 2000, 2000);
        Bitmap floor = Textures.firstLevelFloor;

        // Set floor
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
        Matrix m = new Matrix();
        m.postTranslate(surface.width(),surface.height());
        floorPaint.getShader().setLocalMatrix(m);
    }

    @Override
    public void init() {
        super.init();

        // Set furniture
        gameWorld.addGameObject(GameObjectFactory.
                makeWall((7*cellSize), (2*cellSize)-cellSize/2,
                        cellSize, 4*cellSize-characterScaleFactor*characterDimensionsY,
                        gameWorld)
        );
        gameWorld.addGameObject(GameObjectFactory.
                makeFurniture(
                        bufferWidth/2 + 800,
                        (int)(0.50*bufferHeight),
                        250, 150,
                        gameWorld,
                        Textures.table)
        );
        gameWorld.addGameObject(GameObjectFactory.
                makeTrigger("changelevel",
                        bufferWidth/2 + 500,
                        (int)(0.50*bufferHeight),
                        gameWorld)
        );

        // Set enemies
        gameWorld.addGameObject(GameObjectFactory.
                makeEnemy(
                        124,600,
                        skeletonHealth,
                        Spritesheets.skeletonIdle,
                        StateName.PATROL,
                        Spritesheets.skeletonDeath,
                        gameWorld)
        );

        // Set health
        gameWorld.addGameObject(GameObjectFactory.
                makeHealth(100, 410, gameWorld));
        gameWorld.addGameObject(GameObjectFactory.
                makeHealth(453, 556, gameWorld));
    }

    @Override
    public void handleTrigger(GameObject trigger) {
        if(Objects.equals(trigger.getName(), "changelevel")) {
            gameWorld.changeLevel(new SecondLevel(gameWorld));
        }
    }
}
