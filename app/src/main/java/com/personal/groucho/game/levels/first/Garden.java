package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getSkeleton;
import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.bench;
import static com.personal.groucho.game.assets.Textures.grassFloor;
import static com.personal.groucho.game.assets.Textures.statueBottom;
import static com.personal.groucho.game.assets.Textures.statueWithKey;
import static com.personal.groucho.game.assets.Textures.stone;
import static com.personal.groucho.game.assets.Textures.stoneWall;

import static com.personal.groucho.game.assets.Textures.stoneWall2;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.states.StateName.IDLE;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.components.TextureComponent;
import com.personal.groucho.game.levels.Room;

public class Garden extends Room {
    private final FirstLevel level;
    private int playerPosX, playerPosY;
    private Orientation playerOrientation;
    private int skeletonsCounter;
    private boolean firstTime = true;

    public Garden(GameWorld gameWorld, FirstLevel level) {
        super(2100, 2000, gameWorld);
        this.level = level;
        this.externalWall = stoneWall2;
        this.internalWall = stoneWall;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(grassFloor, 512, 512, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init(){
        super.init();
        playerPosX = (int)(1.1*cellSize);
        playerPosY = 3*cellSize;
        skeletonsCounter = 5;

        if (firstTime) {
            playerOrientation = DOWN;
            String sentence = gameWorld.activity.getString(R.string.groucho_garden_init);
            grouchoTalk(sentence, playerPosX, playerPosY);
            firstTime = false;
        }
        else {
            playerOrientation = RIGHT;
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();
        makeEnemies();
        makeHealth();

        allocateRoom();
    }

    private void makeFurniture() {
        World world = gameWorld.physics.world;;
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                3*cellSize, 2*cellSize, 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                4*cellSize, 3*cellSize, 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                (int) (2.5*cellSize), 7*cellSize, 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                (int) (8.5*cellSize), 8*cellSize, 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                (int) (9.5*cellSize), (int) (3.4*cellSize), 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                (int) (2.4*cellSize), 10*cellSize, 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                (int) (6.5*cellSize), (int) (8.5*cellSize), 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                9*cellSize, 10*cellSize, 100, 90, 10f, world, stone
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                (int) (12.4*cellSize), 3*cellSize, 120, 340, 35f, world, bench
        ));
        gameObjects.add(GameObjectFactory.makeDynamicFurniture(
                (int) (12.4*cellSize), 7*cellSize, 120, 340, 35f, world, bench
        ));
    }

    private void makeTriggers() {
        // Door to entry wall
        makeFloorTrigger(
                (int)(0.35*cellSize), 3*cellSize,
                (int)(-0.35*cellSize), 3*cellSize,
                90, 150, Textures.littleGreenCarpetVer,
                ()->{
                    level.fromZombieRoomToEntryHall = false;
                    level.fromGardenToEntryHall = true;
                    level.fromLibraryToEntryHall = false;
                    level.fromWolfRoomToEntryHall = false;
                    door.play(1f);
                    level.goToEntryHall();
                });

        buildStatue();
    }


    private void makeDecorations() {
        // headstones
        for (int i = 1; i < 6; i++) {
            gameObjects.add(GameObjectFactory.makeWallDecoration(
                    i*2*cellSize + cellSize/2, (int)(0.3*cellSize),
                    100, 160, Textures.headstone
            ));
        }
    }

    private void makeEnemies() {
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (5.5* cellSize), cellSize, RIGHT, getSkeleton(), IDLE, gameWorld));
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (2* cellSize), 6*cellSize, DOWN, getSkeleton(), IDLE, gameWorld));
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (8.5* cellSize), 5*cellSize, RIGHT, getSkeleton(), IDLE, gameWorld));
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (11.5* cellSize), 10*cellSize, LEFT, getSkeleton(), IDLE, gameWorld));
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (11.5* cellSize), 3*cellSize, LEFT, getSkeleton(), IDLE, gameWorld));
    }

    private void makeHealth(){
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.makeHealth(12*cellSize, cellSize, world));
        gameObjects.add(GameObjectFactory.makeHealth(12*cellSize, (int) (9.5*cellSize), world));
        gameObjects.add(GameObjectFactory.makeHealth(3*cellSize, (int) (9.5*cellSize), world));
    }

    private void buildStatue() {
        gameObjects.add(GameObjectFactory.makeFloorDecoration(
                (int) (6.05*cellSize), (int) (5.1*cellSize),
                (float) (4.3*cellSize), 4*cellSize,
                statueBottom
        ));
        GameObject statue = GameObjectFactory.makeStaticFurniture(
                6*cellSize, 4*cellSize,
                (float) (1.7*cellSize), 3*cellSize,
                gameWorld.physics.world,
                statueWithKey
        );
        gameObjects.add(GameObjectFactory.makeTrigger(
                6*cellSize, (int) (4.3*cellSize),
                cellSize, (int) (0.5*cellSize),
                gameWorld.physics.world,
                () -> {
                    if (skeletonsCounter == 0) {
                        if (!level.gardenKey) {
                            TextureComponent textureComp = (TextureComponent) statue.getComponent(ComponentType.DRAWABLE);
                            textureComp.init(Textures.statue, (int) (1.7 * cellSize), 3 * cellSize);
                            level.gardenKey = true;
                            level.counterKeys++;
                            String sentence = gameWorld.activity.getString(R.string.groucho_keys)
                                    + " " + level.counterKeys + ".";
                            grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                        }
                    }
                    else {
                        String sentence = gameWorld.activity.getString(R.string.groucho_garden_statue);
                        grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                    }
                }
        ));
        gameObjects.add(statue);
    }

    @Override
    public void handleDeath() {
        skeletonsCounter--;
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(playerOrientation);
        setBrightness(maxBrightness);
    }
}
