package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Textures.armChairLeft;
import static com.personal.groucho.game.assets.Textures.brownCarpet;
import static com.personal.groucho.game.assets.Textures.brownDoor;
import static com.personal.groucho.game.assets.Textures.dresser;
import static com.personal.groucho.game.assets.Textures.greenCouchRight;
import static com.personal.groucho.game.assets.Textures.greenWall;
import static com.personal.groucho.game.assets.Textures.hanger;
import static com.personal.groucho.game.assets.Textures.heavyDoor;
import static com.personal.groucho.game.assets.Textures.lamp;
import static com.personal.groucho.game.assets.Textures.lightWoodFloor;
import static com.personal.groucho.game.assets.Textures.littleCarpet;
import static com.personal.groucho.game.assets.Textures.littleGreenCarpetVer;
import static com.personal.groucho.game.assets.Textures.littleTable;
import static com.personal.groucho.game.assets.Textures.meFrame;
import static com.personal.groucho.game.assets.Textures.windowInternal;
import static com.personal.groucho.game.assets.Textures.windowNight;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.levels.first.EntryHallEvents.bathroomDoorEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.endGameEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.firstTimeInRoomEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.firstTimeTryOpenHeavyDoorEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.fromBathroomEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.fromGardenEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.fromKitchenEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.fromLibraryEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.gardenDoorEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.kitchenDoorEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.libraryDoorEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.openHeavyDoorEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.talkEvent;
import static com.personal.groucho.game.levels.first.EntryHallEvents.tryOpenHeavyDoorEvent;

import com.google.fpl.liquidfun.World;
import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.levels.Room;

public class EntryHall extends Room {
    protected final FirstLevel level;
    protected int playerPosX, playerPosY;
    protected Orientation playerOrientation = UP;

    public EntryHall(GameWorld gameWorld, FirstLevel level) {
        super(2000, 1500, gameWorld);
        this.internalWall = greenWall;
        this.externalWall = woodWall;
        this.level = level;

        setFloor(lightWoodFloor, 128, 128);
    }

    @Override
    public void init() {
        super.init();

        if (level.isEndGame()) {
            endGameEvent(this);
        }

        if (firstTime) {
            firstTimeInRoomEvent(this);
        }
        else if (level.fromLibraryToEntryHall){
            fromLibraryEvent(this);
        }
        else if (level.fromBathroomToEntryHall) {
            fromBathroomEvent(this);
        }
        else if (level.fromGardenToEntryHall) {
            fromGardenEvent(this);
        }
        else if (level.fromKitchenToEntryHall) {
            fromKitchenEvent(this);
        }

        makeDecorations();
        makeTriggers();
        makeFurniture();
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

    private void makeTriggers() {
        addWallTrigger(3*unit, (int)(9.1*unit), 3*unit, 9*unit,
                160, 280, brownDoor, () -> libraryDoorEvent(this));
        addFloorTrigger((int)(11.55*unit),2*unit, (int)(12.3*unit), 2*unit,
                90, 150, littleGreenCarpetVer, () -> bathroomDoorEvent(this));
        addFloorTrigger((int)(11.55*unit),6*unit, (int)(12.3*unit), 6*unit,
                90, 150, littleGreenCarpetVer, () -> gardenDoorEvent(this));
        addFloorTrigger((int)(0.35*unit), 3*unit, (int)(-0.35*unit), 3*unit,
                90, 150, littleGreenCarpetVer, () -> kitchenDoorEvent(this));
        addWallTrigger((int)(1.5*unit), (int)(-0.25*unit), (int)(1.5*unit), (int)(-0.9*unit),
                180, 250, dresser, () -> talkEvent(this, R.string.groucho_entryhall_dresser));
        addWallTrigger((int)(3.5*unit), -unit, (int)(3.5*unit), (int)(-0.75*unit),
                188, 200, windowNight, () -> talkEvent(this, R.string.groucho_entryhall_window));
        addWallTrigger((int)(8.5*unit), -unit, (int)(8.5*unit), (int)(-0.75*unit),
                188, 200, windowNight, () -> talkEvent(this, R.string.groucho_entryhall_window));
        addWallTrigger((int)(10.5*unit), -unit, (int)(10.5*unit), (int) (-0.60*unit),
                150, 150, meFrame, () -> talkEvent(this, R.string.groucho_entryhall_me_frame));

        Runnable runnable;
        if (firstTime) {
            runnable = () -> firstTimeTryOpenHeavyDoorEvent(this);
        }
        else if (!level.isEndGame()){
            runnable = () -> tryOpenHeavyDoorEvent(this);
        }
        else {
            runnable = () -> openHeavyDoorEvent(this);
        }
        heavyDoorGO = addWallTrigger(6*unit,(int)(-0.85*unit), 6*unit,(int)(-0.95 * unit),
                320,280, heavyDoor, runnable);
    }
    GameObject heavyDoorGO;

    private void makeDecorations() {
        addFloorDec(6*unit, (int)(1.1*unit), 512, 356, brownCarpet);
        addWallDec((int)(4.2*unit), (int)(-0.5*unit), 100, 356, hanger);
        addWallDec((int)(7.8*unit), (int)(-0.5*unit), 100, 356, lamp);
        addFloorDec((int)(1.50*unit), (int)(6.6*unit), 90, 210, littleCarpet);
        addFloorDec((int)(0.35*unit), (int)(3.0*unit), 90, 150, littleGreenCarpetVer);
        addWallDec((int)(7.5*unit), (int)(9.0*unit), 188, 200, windowInternal);
        addWallDec((int)(10.5*unit), (int)(9.0*unit), 188, 200, windowInternal);
    }

    private void makeFurniture() {
        addDynamicFurn((int)(0.80*unit),(int)(6.5*unit),150,200,15f,armChairLeft);
        addDynamicFurn((int)(0.80*unit),(int)(5.5*unit),150,150,5f,littleTable);
        addDynamicFurn((int)(11.3*unit),(int)(0.3*unit),150,150,5f,littleTable);
        addDynamicFurn((int)(11.3*unit),(int)(3.8*unit),150,380,25f,greenCouchRight);
    }

    private void makeHealth() {
        World world = gameWorld.physics.world;
        gameObjects.add(GameObjectFactory.makeHealth(5*unit, (int) (4.5*unit), world));
        gameObjects.add(GameObjectFactory.makeHealth(6*unit, (int) (4.5*unit), world));
        gameObjects.add(GameObjectFactory.makeHealth(7*unit, (int) (4.5*unit), world));
        gameObjects.add(GameObjectFactory.makeHealth(6*unit, (int) (3.5*unit), world));
        gameObjects.add(GameObjectFactory.makeHealth(6*unit, (int) (5.5*unit), world));
    }
}
