package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.UP;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class Hall extends Room {
    public static boolean firstTime = true;
    private final FirstLevel level;

    public Hall(GameWorld gameWorld, FirstLevel level) {
        super(1500, 1500, gameWorld);
        this.level = level;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(Textures.firstLevelFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();

        makeFurniture();
        makeTriggers();
        makeDecorations();

        allocateRoom();

        firstTime = false;
    }

    private void makeDecorations() {

    }

    private void makeTriggers() {
        // Door to hallway
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (2.5*cellSize), (int) (9.1*cellSize),
                        160, 280,
                        Textures.brownDoor
                )));
        gameObjects.add(GameObjectFactory.
                makeTrigger(
                        (int) (2.5*cellSize), (int) (9*cellSize),
                        160, 270,
                        gameWorld.physics.world,
                        () -> {
                            door.play(1f);
                            level.fromHall = true;
                            level.fromGrouchoRoom = false;
                            level.goToHallway();
                        }));

    }

    private void makeFurniture() {

    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos((int) (2.5*cellSize), (int) (7.5*cellSize));
        gameWorld.player.setOrientation(UP);
        setBrightness(maxBrightness);
    }

}
