package com.personal.groucho.game.levels;

import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.Graphics.bufferHeight;
import static com.personal.groucho.game.Graphics.bufferWidth;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.assets.Textures;

import java.util.Objects;

public class FirstLevel extends Level{

    public FirstLevel(GameWorld gameWorld) {
        super(gameWorld, 2000, 2000);

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(Textures.firstLevelFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();
        gameWorld.player.setPos(850, 1000);
        setBrightness(maxBrightness);

        // Set furniture
        gameObjects.add((GameObjectFactory.
                makeFurniture(
                        bufferWidth/2 + 800,
                        (int)(0.50*bufferHeight)+400,
                        250, 150,
                        gameWorld,
                        Textures.table)
        ));

        // Set enemies
        gameObjects.add(GameObjectFactory.
                makeEnemy(
                        124,600,
                        Orientation.DOWN,
                        Spritesheets.skeletonIdle,
                        StateName.PATROL,
                        gameWorld)
        );

        // Set walls
        gameObjects.addAll(GameObjectFactory.
                makeWall(4*cellSize - cellSize/2, -cellSize, 5*cellSize, gameWorld));

        gameObjects.add(GameObjectFactory.makeTrigger("changelevel", 800, 800, gameWorld));

        for (GameObject go : gameObjects) {
            gameWorld.goHandler.addGameObject(go);
        }
    }

    @Override
    public void handleTrigger(GameObject trigger) {
        if(Objects.equals(trigger.name, "changelevel")) {
            gameWorld.changeLevel(new SecondLevel(gameWorld));
        }
    }
}
