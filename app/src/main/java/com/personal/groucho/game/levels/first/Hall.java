package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.grouchoBubble;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.UP;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class Hall extends Room {
    public static boolean firstTime = true;
    private GameObject grouchoTrigger;
    private final FirstLevel level;
    private int playerPosX, playerPosY;

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

        playerPosX = (int) (2.5*cellSize);
        playerPosY = (int) (7.5*cellSize);

        if (firstTime) {
            setControllerVisibility(false);
            grouchoTalk(gameWorld.activity.getString(R.string.groucho_level1_hall_talk_init1), playerPosX, playerPosY);
            level.eventChain.addAction(()->gameWorld.player.setOrientation(DOWN));
            level.eventChain.addAction(()->grouchoTalk(gameWorld.activity.getString(R.string.groucho_level1_hall_talk_init2), playerPosX, playerPosY));
            level.eventChain.addAction(()->setControllerVisibility(true));
        }

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
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(UP);
        setBrightness(maxBrightness);
    }


}
