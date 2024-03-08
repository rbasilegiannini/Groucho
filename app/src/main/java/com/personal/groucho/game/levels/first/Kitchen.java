package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getWolf;
import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.fridge;
import static com.personal.groucho.game.assets.Textures.kitchen;
import static com.personal.groucho.game.assets.Textures.lightWoodFloor;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.states.StateName.IDLE;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class Kitchen extends Room {
    private final FirstLevel level;
    private int playerPosX, playerPosY;
    private boolean firstTime = true;

    public Kitchen(GameWorld gameWorld, FirstLevel level) {
        super(1650, 1650, gameWorld);
        this.internalWall = Textures.greenWall;
        this.externalWall = Textures.woodWall;
        this.level = level;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(lightWoodFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();
        playerPosX = (int)(9.1*cellSize);
        playerPosY = 4*cellSize;

        if (firstTime){
            String sentence = gameWorld.activity.getString(R.string.groucho_kitchen_init);
            grouchoTalk(sentence, playerPosX, playerPosY);
            firstTime = false;
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();
        makeEnemies();
        makeHealth();

        allocateRoom();
    }

    @Override
    public void allocateRoom() {
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(LEFT);
        setBrightness(maxBrightness);
    }

    private void makeFurniture() {
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture((int)(9.4*cellSize), (int) (7*cellSize),
                        150, 380, 25f, world, Textures.greenCouchRight)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture((int)(9.4*cellSize), (int) (5.5*cellSize),
                        150, 150, 5f, world, Textures.littleTable)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int)(9.6*cellSize), (int) (1.5*cellSize), 90, 400, 35f,
                        world, Textures.dresserRight)
        );
        gameObjects.add(GameObjectFactory.
                makeStaticFurniture(
                (int) (1.7*cellSize), (int)(0.5*cellSize),
                550, 400,
                world, kitchen)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int)(3.1*cellSize), (int) (6.7*cellSize), 250, 360, 35f,
                        world, Textures.bigTable)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int)(2.1*cellSize), (int) (6.7*cellSize), 90, 150, 5f,
                        world, Textures.chairLeft)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int)(4.1*cellSize), (int) (6.7*cellSize), 90, 150, 5f,
                        world, Textures.chairRight)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int)(3.1*cellSize), (int) (5.3*cellSize), 90, 150, 5f,
                        world, Textures.chairUp)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int)(2.9*cellSize), (int) (7.7*cellSize), 90, 110, 5f,
                        world, Textures.chairDown)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (7.8*cellSize), (int) (7.8*cellSize), 110, 110, 5f,
                        world, Textures.box)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (7.4*cellSize), (int) (8.1*cellSize), 60, 60, 5f,
                        world, Textures.box)
        );
    }

    private void makeTriggers(){
        // Door to entry hall
        makeFloorTrigger(
                (int)(9.6*cellSize), 4*cellSize,
                (int)(10.3*cellSize), 4*cellSize,
                90, 150, Textures.littleGreenCarpetVer,
                ()->{
                    level.fromZombieRoomToEntryHall = false;
                    level.fromGardenToEntryHall = false;
                    level.fromLibraryToEntryHall = false;
                    level.fromKitchenToEntryHall = true;
                    door.play(1f);
                    level.goToEntryHall();
                });
    }

    private void makeDecorations() {
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (9.3*cellSize), (int) (-0.5*cellSize),
                        100, 356,
                        Textures.lamp
                )));
        gameObjects.add((GameObjectFactory.
                makeWallDecoration(
                        (int) (7*cellSize), (int) (-0.5*cellSize),
                        400, 400, Textures.library
                )));
        gameObjects.add(GameObjectFactory.
                makeWallDecoration(
                4*cellSize, (int)(-0.5*cellSize),
                150, 400, fridge)
        );
        gameObjects.add(GameObjectFactory.
                makeFloorDecoration(
                        (int) (2.3*cellSize), (int) (2.1*cellSize),
                        512, 356, Textures.brownCarpet)
        );
    }

    private void makeEnemies() {
        gameObjects.add(GameObjectFactory.
                makeEnemy(6*cellSize, 4*cellSize, LEFT, getWolf(), IDLE, gameWorld));
    }

    private void makeHealth() {
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.makeHealth((int) (4.8*cellSize), cellSize/2, world));
        gameObjects.add(GameObjectFactory.makeHealth((int) (5.4*cellSize), cellSize/2, world));
        gameObjects.add(GameObjectFactory.makeHealth(8*cellSize, (int) (8.1*cellSize), world));
    }

    @Override
    public void handleDeath() {
        level.kitchenKey = true;
    }
}
