package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.orangeWall;
import static com.personal.groucho.game.assets.Textures.woodFloor;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.UP;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class Hallway extends Room {
    public static boolean firstTime = true;
    private final FirstLevel level;
    private int playerPosX, playerPosY;
    private int tableX, tableY;
    private Orientation playerOrientation = UP;

    public Hallway(GameWorld gameWorld, FirstLevel level) {
        super(2000, 600, gameWorld);
        this.level = level;
        this.internalWall = orangeWall;
        this.externalWall = woodWall;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(woodFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();
        tableX = 6*cellSize;
        tableY = (int) (0.75*cellSize);

        if (level.fromGrouchoRoomToHallway) {
            playerPosX = (int) (2.5*cellSize);
            playerPosY = (int) (1.7*cellSize);
            playerOrientation = UP;
        }
        else if (level.fromLibraryToHallway) {
            playerPosX = (int) (10.5*cellSize);
            playerPosY = cellSize;
            playerOrientation = DOWN;
        }

        if (firstTime) {
            setControllerVisibility(false);

            grouchoTalk(gameWorld.activity.getString(R.string.groucho_hallway_talk_init1), playerPosX, playerPosY);
            level.eventChain.addAction(()-> {
                gameWorld.controller.bulb.setVisibility(true);
                grouchoTalk(gameWorld.activity.getString(R.string.groucho_hallway_talk_init2), playerPosX, playerPosY);
            });
            level.eventChain.addAction(()-> {
                gameWorld.controller.handleLightTouchDown();
                gameWorld.controller.dpad.setVisibility(true);
                gameWorld.controller.pause.setVisibility(true);
            });
        }

        makeTriggers();
        makeDecorations();
        makeFurniture();

        allocateRoom();

        firstTime = false;
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();

        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(playerOrientation);
        gameWorld.player.rest();
        setBrightness(minBrightness);
    }

    private void makeFurniture(){
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(tableX, tableY, 150, 150, 5f, world, Textures.littleTable)
        );
    }

    private void makeTriggers() {
        // Door to GrouchoRoom
        makeWallTrigger(
                (int)(2.5*cellSize), (int)(3.1*cellSize),
                (int) (2.5*cellSize), (int) (2.95*cellSize),
                160, 280,
                Textures.brownDoor,
                () -> {
                    door.play(1f);
                    level.goToGrouchoRoom();
                }
        );

        // Door to library
        makeWallTrigger(
                (int)(10.5*cellSize), (int)(-0.85*cellSize),
                (int)(10.5*cellSize), (int)(-0.95*cellSize),
                160, 280,
                Textures.brownDoor,
                () -> {
                    door.play(1f);
                    level.fromHallwayToLibrary = true;
                    level.fromEntryHallToLibrary = false;
                    level.goToLibrary();
                }
        );

        // Little library
        makeWallTrigger(
                (int)(8.5*cellSize), (int)(-0.25*cellSize),
                (int)(8.5*cellSize), (int)(-0.5*cellSize),
                180, 128,
                Textures.littleLibrary,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_hallway_talk_library);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                }
        );

        // Window
        makeWallTrigger(
                (int)(2.5*cellSize), -cellSize,
                (int)(2.5*cellSize), (int)(-0.75*cellSize),
                188, 200,
                Textures.windowNight,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_hallway_talk_window);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                }
        );
    }

    private void makeDecorations() {
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int)(10.5*cellSize), (int)(0.4*cellSize),
                        170, 90,
                        Textures.littleGreenCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int)(6*cellSize), (int)(1.5*cellSize),
                        512, 356,
                        Textures.brownCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int)(6*cellSize), 0,
                        400, 210,
                        Textures.orangeCouch
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        cellSize, (int)(-0.25*cellSize),
                        180, 250,
                        Textures.dresser
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int)(7.5*cellSize), (int)(3*cellSize),
                        188, 200,
                        Textures.windowNight
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int)(10.5*cellSize), (int)(3*cellSize),
                        188, 200,
                        Textures.windowNight
                )));
    }
}
