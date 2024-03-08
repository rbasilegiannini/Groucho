package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getZombie;
import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.bathroomFloor;
import static com.personal.groucho.game.assets.Textures.bathroomWall;
import static com.personal.groucho.game.assets.Textures.littleDresser;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.controller.states.StateName.IDLE;
import static com.personal.groucho.game.controller.states.StateName.PATROL;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.components.TextureComponent;
import com.personal.groucho.game.levels.Room;

public class Bathroom extends Room {
    private final FirstLevel level;
    private int playerPosX, playerPosY;
    private GameObject bathroomKey;

    public static boolean firstTime = true;

    public Bathroom(GameWorld gameWorld, FirstLevel level) {
        super(3000, 1200, gameWorld);
        this.internalWall = bathroomWall;
        this.externalWall = woodWall;
        this.level = level;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(bathroomFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init(){
        super.init();
        playerPosX = (int)(1.1*cellSize);
        playerPosY = 3*cellSize;

        if (firstTime) {
            String sentence = gameWorld.activity.getString(R.string.groucho_bathroom_init);
            grouchoTalk(sentence, playerPosX, playerPosY);
        }

        makeWalls();
        makeFurniture();
        makeTriggers();
        makeDecorations();
        makeEnemies();

        allocateRoom();

        firstTime = false;
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(RIGHT);
        setBrightness(minBrightness);
    }

    private void makeWalls() {
        int wallCenterY = (int)(-0.5 * cellSize);
        int wallDim = 6 * cellSize;
        World world = gameWorld.physics.world;
        for (int i = 2; i < 8; i++) {
            gameObjects.addAll(
                    GameObjectFactory.makeWall((int) (i*2 * cellSize + 0.5*cellSize), wallCenterY, wallDim, world));
        }
    }

    private void makeDecorations() {
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int)(0.85*cellSize), 0, 70, 140, Textures.littleGrass));
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int)(3.5*cellSize), 0, 70, 140, Textures.littleGrass));
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int) (5.5* cellSize), 0, 250, 250, Textures.wc));
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int) (7.5* cellSize), 0, 250, 250, Textures.wc));
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int) (9.5* cellSize), 0, 250, 250, Textures.wc));
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int) (11.5* cellSize), 0, 250, 250, Textures.wc));
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int) (13.5* cellSize), 0, 250, 250, Textures.wc));
        gameObjects.add(GameObjectFactory.makeWallDecoration(
                (int) (2.2* cellSize), -cellSize/2, 250, 380, Textures.sink));
        gameObjects.add(GameObjectFactory.makeFloorDecoration(
                (int) (2.2* cellSize), cellSize, 250, 100, Textures.littleCarpetBathroom));
        gameObjects.add((GameObjectFactory.makeWallDecoration(
                        (int) (15.2*cellSize), (int) (-0.5*cellSize), 100, 356, Textures.lamp)));
    }

    private void makeTriggers() {
        makeWallTrigger(
                (int)(16.3*cellSize), -cellSize,
                (int)(16.0*cellSize), (int)(-0.75*cellSize),
                188, 200,
                Textures.windowNight,
                () -> {
                    String sentence = gameWorld.activity.getString(R.string.groucho_entryhall_window);
                    grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                });

        bathroomKey = makeWallTrigger(
                (int)(17.3*cellSize), 0,
                (int)(17.3*cellSize), -cellSize/4,
                100, 100,
                Textures.littleDresserWithKey,
                () -> {
                    if (!level.bathroomKey) {
                        TextureComponent textureComp = (TextureComponent) bathroomKey.getComponent(ComponentType.DRAWABLE);
                        textureComp.init(littleDresser, 100, 100);
                        level.bathroomKey = true;
                        level.counterKeys++;
                        String sentence = gameWorld.activity.getString(R.string.groucho_keys)
                                + " " + level.counterKeys + ".";
                        grouchoTalk(sentence, gameWorld.player.posX, gameWorld.player.posY);
                    }
                });
        makeFloorTrigger(
                (int)(0.35*cellSize), 3*cellSize,
                (int)(-0.35*cellSize), 3*cellSize,
                90, 150, Textures.littleGreenCarpetVer,
                ()->{
                    level.fromBathroomToEntryHall = true;
                    level.fromGardenToEntryHall = false;
                    level.fromLibraryToEntryHall = false;
                    level.fromKitchenToEntryHall = false;
                    door.play(1f);
                    level.goToEntryHall();
                });
    }
    private void makeFurniture() {
        World world = gameWorld.physics.world;

        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        5*cellSize, 5*cellSize, 90, 150, 5f,
                        world, Textures.chairLeft)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (3.9*cellSize), 2*cellSize, 90, 300, 35f,
                        world, Textures.dresserRight)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (0.5*cellSize), 5*cellSize, 150, 150, 5f,
                        world, Textures.littleTable)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (7.7*cellSize), 4*cellSize, 150, 130, 5f,
                        world, Textures.ironingBoard)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (10.7*cellSize), 5*cellSize, 150, 180, 15f,
                        world, Textures.tableWithFlowers)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (16.7*cellSize), 5*cellSize, 100, 100, 5f,
                        world, Textures.box)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (17.3*cellSize), 5*cellSize, 70, 70, 5f,
                        world, Textures.box)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (17.0*cellSize), (int) (4.5*cellSize), 110, 110, 5f,
                        world, Textures.box)
        );
        gameObjects.add(GameObjectFactory.
                makeDynamicFurniture(
                        (int) (17.7*cellSize), (int) (2*cellSize), 90, 300, 35f,
                        world, Textures.dresserRight)
        );
    }

    private void makeEnemies() {
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (5.5* cellSize), cellSize, DOWN, getZombie(), PATROL, gameWorld));
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (7.5* cellSize), cellSize, DOWN, getZombie(), IDLE, gameWorld));
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (11.5* cellSize), (int) (2.5*cellSize), UP, getZombie(), PATROL, gameWorld));
        gameObjects.add(
                GameObjectFactory.makeEnemy(
                        (int) (13.5* cellSize), (int) (3.3*cellSize), UP, getZombie(), IDLE, gameWorld));
    }
}
