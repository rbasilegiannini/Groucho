package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.brownFloor;
import static com.personal.groucho.game.assets.Textures.orangeWall;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.UP;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class GrouchoRoom extends Room {
    public static boolean firstTime = true;
    private final FirstLevel level;
    private int playerPosX, playerPosY, tableX, tableY, bedX, bedY;

    public GrouchoRoom(GameWorld gameWorld, FirstLevel level) {
        super(1000, 1000, gameWorld);
        this.internalWall = orangeWall;
        this.externalWall = woodWall;
        this.level = level;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(brownFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();
        tableX = 5*cellSize;
        tableY = (int) (3.5*cellSize);
        bedX = 5*cellSize;
        bedY = 2*cellSize;

        if (firstTime) {
            playerPosX = 500;
            playerPosY = 500;

            setControllerVisibility(false);

            grouchoTalk(gameWorld.activity.getString(R.string.groucho_bedroom_init1), playerPosX, playerPosY);
            level.eventChain.addAction(()-> {
                gameWorld.player.setOrientation(UP);
                dylanTalk( gameWorld.activity.getString(R.string.dylan_bedroom_init), 600, 500);
            });
            level.eventChain.addAction(()->
                    grouchoTalk(gameWorld.activity.getString(R.string.groucho_bedroom_init2), playerPosX, playerPosY));
            level.eventChain.addAction(()->{
                gameWorld.controller.dpad.setVisibility(true);
                gameWorld.controller.pause.setVisibility(true);
            });

        }
        else {
            playerPosX = 2*cellSize;
            playerPosY = cellSize;
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();

        allocateRoom();

        firstTime = false;
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        setBrightness(maxBrightness);
    }

    private void makeTriggers() {
        // Groucho's photo
        makeWallTrigger(
                (int)(0.75*cellSize), (int)(-cellSize),
                (int)(0.75*cellSize), (int)(-0.5*cellSize),
                128, 128,
                Textures.grouchoFrame,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_bedroom_photo);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                });

        // Wardrobe
        makeWallTrigger(
                (int)(4.5*cellSize), (int)(-0.5*cellSize),
                (int) (4.5*cellSize), (int) (-1.2*cellSize),
                280, 350,
                Textures.grouchoWardrobe,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_bedroom_wardrobe);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                });

        // Door to hallway
        makeWallTrigger(
                2*cellSize, (int)(-0.85*cellSize),
                2*cellSize, (int) (-0.95*cellSize),
                160, 280,
                Textures.brownDoor,
                () -> {
                    door.play(1f);
                    level.fromLibraryToHallway = false;
                    level.fromGrouchoRoomToHallway = true;
                    level.goToHallway();
                });

    }


    private void makeFurniture() {
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(tableX, tableY, 250, 150, 5f, world, Textures.table)
        );

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(bedX, bedY, 280, 350, 100f, world, Textures.bed)
        );
    }

    private void makeDecorations() {
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int)(2*cellSize), (int)(0.4*cellSize),
                        170, 90,
                        Textures.littleGreenCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        2*cellSize, (int)(2.5*cellSize),
                        512, 380,
                        Textures.redCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int)(1.5*cellSize), (int)(6.0*cellSize),
                        188, 200,
                        Textures.windowInternal
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int)(4.5*cellSize), (int)(6.0*cellSize),
                        188, 200,
                        Textures.windowInternal
                )));
    }
}
