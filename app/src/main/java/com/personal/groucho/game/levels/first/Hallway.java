package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Spritesheets.skeletonIdle;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.UP;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Shader;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class Hallway extends Room {
    private final FirstLevel level;

    public Hallway(GameWorld gameWorld, FirstLevel level) {
        super(2000, 600, gameWorld);
        this.level = level;
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

        makeTriggers();

        allocateRoom();
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();

        gameWorld.player.setPos((int) (2.5*cellSize), (int) (1.7*cellSize));
        gameWorld.player.setOrientation(UP);
        gameWorld.player.rest();
        setBrightness(maxBrightness);
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
                            level.goToGrouchoRoom();
                        }));

        gameObjects.add(GameObjectFactory.makeEnemy(800, 100, DOWN, skeletonIdle, StateName.IDLE,gameWorld));
    }
}
