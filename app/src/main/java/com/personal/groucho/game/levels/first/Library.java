package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getZombie;
import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.orangeWall;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.controller.states.StateName.IDLE;

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

public class Library extends Room {
    public static boolean firstTime = true;
    private final FirstLevel level;
    private int playerPosX, playerPosY;
    private Orientation playerOrientation = UP;
    private int couchX, couchY, tableX, tableY, littleTableX, littleTableY;
    private int chairLeftX, chairLeftY, chairRightX, chairRightY;
    private int chairUpX, chairUpY, chairDownX, chairDownY;

    public Library(GameWorld gameWorld, FirstLevel level) {
        super(1500, 1500, gameWorld);
        this.level = level;
        this.internalWall = orangeWall;
        this.externalWall = woodWall;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(Textures.orangeFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();

        if (level.fromHallwayToLibrary) {
            playerPosX = (int) (4.5 * cellSize);
            playerPosY = (int) (7.5 * cellSize);
            playerOrientation = UP;
        }
        if (level.fromEntryHallToLibrary) {
            playerPosX = (int) (4.5*cellSize);
            playerPosY = cellSize;
            playerOrientation = DOWN;
        }

        couchX = (int) (4.5*cellSize);
        couchY = 3*cellSize;
        tableX = (int) (4.5*cellSize);
        tableY = (int) (4.5*cellSize);

        littleTableX = (int) (1.5*cellSize);
        littleTableY = (int) (6*cellSize);
        chairRightX = (int) (8.5*cellSize);
        chairRightY = (int) littleTableY;
        chairLeftX = (int) (0.5*cellSize);
        chairLeftY = (int) chairRightY;
        chairUpX = (int) (1.5*cellSize);
        chairUpY = (int) (5*cellSize);
        chairDownX = (int) (7.5*cellSize);
        chairDownY = (int) (7*cellSize);

        if (firstTime) {
            setControllerVisibility(false);
            grouchoTalk(gameWorld.activity.getString(R.string.groucho_hall_talk_init1), playerPosX, playerPosY);
            level.eventChain.addAction(()-> {
                gameWorld.player.setOrientation(DOWN);
                grouchoTalk(gameWorld.activity.getString(R.string.groucho_hall_talk_init2), playerPosX, playerPosY);
            });
            level.eventChain.addAction(()->setControllerVisibility(true));
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();
//        makeEnemies();

        allocateRoom();

        firstTime = false;
    }

    private void makeEnemies(){
        gameObjects.add(
                GameObjectFactory.makeEnemy((int) (4.5*cellSize), (int) (5.5*cellSize), UP, getZombie(), IDLE,gameWorld)
        );
    }

    private void makeDecorations() {
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int) (4.5*cellSize),
                        (int) (0.4*cellSize),
                        170,
                        90,
                        Textures.littleGreenCarpet
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (2*cellSize),
                        (int) (-0.5*cellSize),
                        400,
                        400,
                        Textures.library
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (7*cellSize),
                        (int) (-0.5*cellSize),
                        400,
                        400,
                        Textures.library
                )));
        gameObjects.add((GameObjectFactory.
                makeFloorDecoration(
                        (int) (4.5*cellSize),
                        (int) (4.5*cellSize),
                        700,
                        450,
                        Textures.redCarpet
                )));
    }

    private void makeTriggers() {
        // Door to hallway
        makeWallTrigger(
                (int)(4.5*cellSize), (int)(9.1*cellSize),
                (int) (4.5*cellSize), (int) (9*cellSize),
                160, 280,
                Textures.brownDoor,
                () -> {
                    door.play(1f);
                    level.fromLibraryToHallway = true;
                    level.fromGrouchoRoomToHallway = false;
                    level.goToHallway();
                }
        );

        // Door to entry hall
        makeWallTrigger(
                (int)(4.5*cellSize), (int)(-0.85*cellSize),
                (int)(4.5*cellSize), (int)(-0.95*cellSize),
                160, 280,
                Textures.brownDoor,
                () -> {
                    // if key
                    door.play(1f);
                    level.fromLibraryToEntryHall = true;
                    level.goToEntryHall();
                }
        );
    }


    private void makeFurniture() {
        World world = gameWorld.physics.world;

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(couchX, couchY, 400, 210, 25f, world, Textures.orangeCouch)
        );

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(tableX, tableY, 250, 150, 5f, world, Textures.mediumTable)
        );

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(littleTableX, littleTableY, 150, 150, 5f, world, Textures.littleTable)
        );

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(chairLeftX, chairLeftY, 90, 150, 5f, world, Textures.chairLeft)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(chairRightX, chairRightY, 90, 150, 5f, world, Textures.chairRight)
        );

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(chairUpX, chairUpY, 90, 150, 5f, world, Textures.chairUp)
        );

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(chairDownX, chairDownY, 90, 110, 5f, world, Textures.chairDown)
        );

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture((int)(6.5*cellSize), (int)(4.5*cellSize), 150, 200, 15f, world, Textures.armChairRight)
        );
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(playerOrientation);
        setBrightness(minBrightness);
    }
}
