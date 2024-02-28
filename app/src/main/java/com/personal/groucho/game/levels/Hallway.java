package com.personal.groucho.game.levels;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.UP;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;

public class Hallway extends Level {

    public Hallway(GameWorld gameWorld) {
        super(gameWorld, 2000, 600);
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
        gameWorld.player.setPos((int) (2.5*cellSize), (int) (1.7*cellSize));
        gameWorld.player.setOrientation(UP);
        gameWorld.player.rest();

        makeTriggers();

        for (GameObject go : gameObjects) {
            gameWorld.goHandler.addGameObject(go);
        }
    }

    private void makeDecorations() {

    }

    private void makeTriggers() {

        // Door to GrouchoRoom
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (2.5*cellSize),
                        (int) (3.1*cellSize),
                        160,
                        280,
                        gameWorld,
                        Textures.brownDoor
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger(
                        (int) (2.5*cellSize), (int) (3.0*cellSize),
                        160, 270,
                        gameWorld,
                        () -> {
                            door.play(1f);
                            gameWorld.changeLevel(new GrouchoRoom(gameWorld));
                        }));
    }
}
