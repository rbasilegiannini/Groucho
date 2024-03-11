package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getSkeleton;
import static com.personal.groucho.game.assets.Textures.bench;
import static com.personal.groucho.game.assets.Textures.grassFloor;
import static com.personal.groucho.game.assets.Textures.headstone;
import static com.personal.groucho.game.assets.Textures.littleGreenCarpetVer;
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
import static com.personal.groucho.game.levels.first.GardenEvents.entryHallDoorEvent;
import static com.personal.groucho.game.levels.first.GardenEvents.firstTimeInRoomEvent;
import static com.personal.groucho.game.levels.first.GardenEvents.statueWithKeyEvent;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class Garden extends Room {
    protected final FirstLevel level;
    protected int playerPosX, playerPosY;
    protected Orientation playerOrientation;
    protected GameObject statue;
    protected int skeletonsCounter;

    public Garden(GameWorld gameWorld, FirstLevel level) {
        super(2100, 2000, gameWorld);
        this.level = level;
        this.externalWall = stoneWall2;
        this.internalWall = stoneWall;

        setFloor(grassFloor, 512, 512);
    }

    @Override
    public void init(){
        super.init();
        playerPosX = (int)(1.1*cellSize);
        playerPosY = 3*cellSize;
        skeletonsCounter = 5;

        if (firstTime) {
            firstTimeInRoomEvent(this);
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

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(playerOrientation);
        setBrightness(maxBrightness);
    }

    private void makeFurniture() {
        addDynamicFurn(3*unit, 2*unit, 100, 90, 10f, stone);
        addDynamicFurn(4*unit, 3*unit, 100, 90, 10f, stone);
        addDynamicFurn((int)(2.5*unit), 7*unit, 100, 90, 10f, stone);
        addDynamicFurn((int)(8.5*unit), 8*unit, 100, 90, 10f, stone);
        addDynamicFurn((int)(9.5*unit), (int)(3.4*unit), 100, 90, 10f, stone);
        addDynamicFurn((int)(2.4*unit), 10*unit, 100, 90, 10f, stone);
        addDynamicFurn((int)(6.5*unit), (int)(8.5*unit), 100, 90, 10f, stone);
        addDynamicFurn(9*unit, 10*unit, 100, 90, 10f, stone);
        addDynamicFurn((int)(12.4*unit), 3*unit, 120, 340, 35f, bench);
        addDynamicFurn((int)(12.4*unit), 7*unit, 120, 340, 35f, bench);
    }

    private void makeTriggers() {
        addFloorTrigger((int)(0.35*unit), 3*unit, (int)(-0.35*unit), 3*unit,
                90, 150, littleGreenCarpetVer, ()->entryHallDoorEvent(this));

        buildStatue();
    }

    private void makeDecorations() {
        for (int i = 1; i < 6; i++) {
            addWallDec(i*2*unit + unit/2, (int)(0.3*unit), 100, 160, headstone);
        }
    }

    private void makeEnemies() {
        addEnemy((int) (5.5*unit), unit, RIGHT, getSkeleton(), IDLE);
        addEnemy(2* unit, 6*unit, DOWN, getSkeleton(), IDLE);
        addEnemy((int) (8.5* unit), 5*unit, RIGHT, getSkeleton(), IDLE);
        addEnemy((int) (11.5* unit), 10*unit, LEFT, getSkeleton(), IDLE);
        addEnemy( (int) (11.5* unit), 3*unit, LEFT, getSkeleton(), IDLE);
    }

    private void makeHealth(){
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.makeHealth(12*cellSize, cellSize, world));
        gameObjects.add(GameObjectFactory.makeHealth(12*cellSize, (int) (9.5*cellSize), world));
        gameObjects.add(GameObjectFactory.makeHealth(3*cellSize, (int) (9.5*cellSize), world));
    }

    private void buildStatue() {
        addFloorDec((int)(6.05*unit),(int)(5.1*unit),(float)(4.3*unit), 4*unit, statueBottom);
        statue = makeStaticFurn(6*unit, 4*unit, (float)(1.7*unit), 3*unit,
                statueWithKey);
        gameObjects.add(GameObjectFactory
                .makeTrigger(6*unit, (int) (4.3*unit), unit, (int) (0.5*unit),
                gameWorld.physics.world, () -> statueWithKeyEvent(this)));
        gameObjects.add(statue);
    }

    @Override
    public void handleDeath() {skeletonsCounter--;}

}
