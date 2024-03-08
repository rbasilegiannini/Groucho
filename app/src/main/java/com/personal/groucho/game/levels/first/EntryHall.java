package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.greenWall;
import static com.personal.groucho.game.assets.Textures.lightWoodFloor;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
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

public class EntryHall extends Room {
    public static boolean firstTime = true;
    private final FirstLevel level;
    private int playerPosX, playerPosY;
    private Orientation playerOrientation = UP;

    public EntryHall(GameWorld gameWorld, FirstLevel level) {
        super(2000, 1500, gameWorld);
        this.internalWall = greenWall;
        this.externalWall = woodWall;
        this.level = level;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(lightWoodFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();

        if (level.isEndGame()) {

        }

        if (firstTime) {
            playerPosX = 3*cellSize;
            playerPosY = (int) (7.5*cellSize);
            String sentence = gameWorld.activity.getString(R.string.dylan_bedroom_init);
            dylanTalk(sentence, playerPosX+2*cellSize, (int) playerPosY);
        }

        if (level.fromLibraryToEntryHall){
            playerPosX = 3*cellSize;
            playerPosY = (int) (7.5*cellSize);
            playerOrientation = UP;
        }

        if (level.fromBathroomToEntryHall) {
            playerPosX = (int) (11*cellSize);
            playerPosY = (int) (2.0*cellSize);
            playerOrientation = LEFT;
        }

        if (level.fromGardenToEntryHall) {
            playerPosX = (int) (11*cellSize);
            playerPosY = (int) (6.0*cellSize);
            playerOrientation = LEFT;
        }

        if (level.fromKitchenToEntryHall) {
            playerPosX = cellSize;
            playerPosY = 3*cellSize;
            playerOrientation = RIGHT;
        }

        if (level.kitchenKey) {
            String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_afterwolf);
            grouchoTalk(sentence, playerPosX, playerPosY);
        }

        makeDecorations();
        makeTriggers();
        makeFurniture();

        allocateRoom();
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(playerOrientation);

        setBrightness(maxBrightness);
    }

    private void makeTriggers() {
        // Door to library
        makeWallTrigger(
                3*cellSize, (int) (9.1*cellSize),
                3*cellSize, (int) (9*cellSize),
                160, 280, Textures.brownDoor,
                () -> {
                    if (!firstTime) {
                        door.play(1f);
                        level.fromEntryHallToLibrary = true;
                        level.fromHallwayToLibrary = false;
                        level.goToLibrary();
                    }
                    else {
                        String sentence = gameWorld.activity.getString(R.string.groucho_library_closed);
                        grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                    }
                });

        // Door to bathroom
        makeFloorTrigger(
                (int) (11.55*cellSize),2*cellSize,
                (int) (12.3*cellSize), 2*cellSize,
                90, 150, Textures.littleGreenCarpetVer,
                ()->{
                    if (!firstTime) {
                        if (!level.bathroomKey) {
                            door.play(1f);
                            level.goToBathroom();
                        } else {
                            String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_room_complete);
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }
                    }
                    else {
                        String sentence = gameWorld.activity.getString(R.string.groucho_entrywall_closed_doors);
                        grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                    }
                });

        // Door to garden
        makeFloorTrigger(
                (int) (11.55*cellSize),6*cellSize,
                (int) (12.3*cellSize), 6*cellSize,
                90, 150, Textures.littleGreenCarpetVer,
                ()->{
                    if (!firstTime) {
                        if (!level.gardenKey) {
                            door.play(1f);
                            level.goToGarden();
                        } else {
                            String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_room_complete);
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }
                    }
                    else {
                        String sentence = gameWorld.activity.getString(R.string.groucho_entrywall_closed_doors);
                        grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                    }
                });

        // Door to kitchen
        makeFloorTrigger(
                (int)(0.35*cellSize), 3*cellSize,
                (int) (-0.35*cellSize), 3*cellSize,
                90, 150, Textures.littleGreenCarpetVer,
                ()->{
                    if(!firstTime) {
                        if (!level.kitchenKey) {
                            door.play(1f);
                            level.goToKitchen();
                        } else {
                            String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_room_complete);
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }
                    }
                    else {
                        String sentence = gameWorld.activity.getString(R.string.groucho_entrywall_closed_doors);
                        grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                    }
                });

        // Dresser
        makeWallTrigger(
                (int) (1.5*cellSize), (int) (-0.25*cellSize),
                (int)(1.5*cellSize), (int) (-0.9*cellSize),
                180, 250,
                Textures.dresser,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_dresser);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                });

        // Windows
        makeWallTrigger(
                (int)(3.5*cellSize), -cellSize,
                (int)(3.5*cellSize), (int)(-0.75*cellSize),
                188, 200,
                Textures.windowNight,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_window);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                });
        makeWallTrigger(
                (int)(8.5*cellSize), -cellSize,
                (int)(8.5*cellSize), (int)(-0.75*cellSize),
                188, 200,
                Textures.windowNight,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_window);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                });

        // Frame
        makeWallTrigger(
                (int)(10.5*cellSize), -cellSize,
                (int)(10.5*cellSize), (int) (-0.60*cellSize),
                150, 150, Textures.meFrame,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_me_frame);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                });

        // Heavy door
        Runnable runnable;
        if (firstTime) {
            runnable = () -> {
                dylanTalk(gameWorld.activity.getString(R.string.dylan_entryhall_heavydoor),
                        (int) (9.5 * cellSize), cellSize);
                level.eventChain.addAction(() ->
                        grouchoTalk(gameWorld.activity.getString(R.string.groucho_entryhall_heavydoor_init),
                                gameWorld.player.posX, gameWorld.player.posY));
                firstTime = false;
            };
        }
        else if (!level.isEndGame()){
            runnable = () -> {
                dylanTalk(gameWorld.activity.getString(R.string.dylan_entryhall_heavydoor),
                        (int) (9.5 * cellSize), cellSize);
                level.eventChain.addAction(
                        () -> grouchoTalk(gameWorld.activity.getString(R.string.groucho_entryhall_heavydoor),
                                gameWorld.player.posX, gameWorld.player.posY));
            };
        }
        else {
            runnable = () -> {
                // Open door (change texture door)

                door.play(1f);

                String sentence = gameWorld.activity.getString(R.string.groucho_endgame_init1);
                grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                level.eventChain.addAction(()->{/*throw sound */});
                level.eventChain.addAction(()->gameWorld.player.setOrientation(DOWN));
                level.eventChain.addAction(()->{
                    String sentence2 = gameWorld.activity.getString(R.string.groucho_endgame_init2);
                    grouchoTalk(sentence2, gameWorld.player.posX, gameWorld.player.posY);
                });
                level.eventChain.addAction(()->{gameWorld.complete = true;});
            };
        }
        makeWallTrigger(
                6*cellSize, (int) (-0.85*cellSize),
                6 * cellSize, (int) (-0.95 * cellSize),
                320, 280,
                Textures.heavyDoor, runnable);
    }

    private void makeDecorations() {
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int) (6*cellSize), (int) (1.1*cellSize),
                        512, 356,
                        Textures.brownCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (4.2*cellSize), (int) (-0.5*cellSize),
                        100, 356,
                        Textures.hanger
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (7.8*cellSize), (int) (-0.5*cellSize),
                        100, 356,
                        Textures.lamp
                )));
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int)(1.50*cellSize), (int)(6.6*cellSize),
                        90, 210,
                        Textures.littleCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int)(0.35*cellSize), (int)(3.0*cellSize),
                        90, 150,
                        Textures.littleGreenCarpetVer
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (7.5*cellSize), (int) (9.0*cellSize),
                        188, 200,
                        Textures.windowInternal
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (10.5*cellSize), (int) (9.0*cellSize),
                        188, 200,
                        Textures.windowInternal
                )));
    }

    private void makeFurniture() {
        World world = gameWorld.physics.world;

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture((int)(0.80*cellSize), (int) (6.5*cellSize),
                        150, 200, 15f, world, Textures.armChairLeft)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture((int)(0.80*cellSize), (int) (5.5*cellSize),
                        150, 150, 5f, world, Textures.littleTable)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture((int)(11.3*cellSize), (int) (0.3*cellSize),
                        150, 150, 5f, world, Textures.littleTable)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture((int)(11.3*cellSize), (int) (3.8*cellSize),
                        150, 380, 25f, world, Textures.greenCouchRight)
        );
    }
}
