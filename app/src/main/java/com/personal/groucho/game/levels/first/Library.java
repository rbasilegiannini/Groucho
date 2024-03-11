package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getZombie;
import static com.personal.groucho.game.assets.Textures.armChairRight;
import static com.personal.groucho.game.assets.Textures.brownDoor;
import static com.personal.groucho.game.assets.Textures.chairDown;
import static com.personal.groucho.game.assets.Textures.chairLeft;
import static com.personal.groucho.game.assets.Textures.chairRight;
import static com.personal.groucho.game.assets.Textures.chairUp;
import static com.personal.groucho.game.assets.Textures.library;
import static com.personal.groucho.game.assets.Textures.littleGreenCarpet;
import static com.personal.groucho.game.assets.Textures.littleTable;
import static com.personal.groucho.game.assets.Textures.mediumTable;
import static com.personal.groucho.game.assets.Textures.orangeCouch;
import static com.personal.groucho.game.assets.Textures.orangeFloor;
import static com.personal.groucho.game.assets.Textures.orangeWall;
import static com.personal.groucho.game.assets.Textures.redCarpet;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.controller.states.StateName.IDLE;
import static com.personal.groucho.game.levels.first.LibraryEvents.entryHallDoorEvent;
import static com.personal.groucho.game.levels.first.LibraryEvents.firstTimeInRoomEvent;
import static com.personal.groucho.game.levels.first.LibraryEvents.fromEntryWallEvent;
import static com.personal.groucho.game.levels.first.LibraryEvents.fromHallwayEvent;
import static com.personal.groucho.game.levels.first.LibraryEvents.hallwayDoorEvent;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.levels.Room;

public class Library extends Room {
    protected final FirstLevel level;
    protected int playerPosX, playerPosY;
    protected Orientation playerOrientation = UP;
    protected boolean isZombieDead = false;

    public Library(GameWorld gameWorld, FirstLevel level) {
        super(1500, 1500, gameWorld);
        this.level = level;
        this.internalWall = orangeWall;
        this.externalWall = woodWall;

        setFloor(orangeFloor, 128, 128);
    }

    @Override
    public void init() {
        super.init();

        if (firstTime) {
            firstTimeInRoomEvent(this);
        }

        if (level.fromHallwayToLibrary) {
            fromHallwayEvent(this);
        }
        else if (level.fromEntryHallToLibrary) {
            fromEntryWallEvent(this);
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();
        makeEnemies();

        allocateRoom();
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        gameWorld.player.setOrientation(playerOrientation);
        setBrightness(minBrightness);
    }

    private void makeFurniture() {
        addDynamicFurn((int)(4.5*unit), 3*unit, 400, 210, 25f, orangeCouch);
        addDynamicFurn((int)(4.5*unit), (int)(4.5*unit), 250, 150, 5f, mediumTable);
        addDynamicFurn((int)(1.5*unit), 6*unit, 150, 150, 5f, littleTable);
        addDynamicFurn((int)(0.5*unit), 6*unit, 90, 150, 5f, chairLeft);
        addDynamicFurn((int)(8.5*unit), 6*unit, 90, 150, 5f, chairRight);
        addDynamicFurn((int)(1.5*unit), 5*unit, 90, 150, 5f, chairUp);
        addDynamicFurn((int)(7.5*unit), 7*unit, 90, 110, 5f, chairDown);
        addDynamicFurn((int)(6.5*unit), (int)(4.5*unit), 150, 200, 15f, armChairRight);
    }

    private void makeTriggers() {
        addWallTrigger((int)(4.5*unit), (int)(9.1*unit), (int)(4.5*unit), 9*unit,
                160, 280, brownDoor, () -> hallwayDoorEvent(this));
        addWallTrigger((int)(4.5*unit), (int)(-0.85*unit), (int)(4.5*unit), (int)(-0.95*unit),
                160, 280, brownDoor, () -> entryHallDoorEvent(this));
    }

    private void makeDecorations() {
        addFloorDec((int)(4.5*unit), (int)(0.4*unit), 170, 90, littleGreenCarpet);
        addWallDec(2*unit, (int)(-0.5*unit), 400, 400, library);
        addWallDec(7*cellSize, (int)(-0.5*cellSize), 400, 400, library);
        addFloorDec((int)(4.5*cellSize), (int)(4.5*cellSize), 700, 450, redCarpet);
    }

    private void makeEnemies(){
        if (!isZombieDead)
            addEnemy((int) (4.5*cellSize), (int) (5.5*cellSize), UP, getZombie(), IDLE);
    }

    @Override
    public void handleDeath() {
        isZombieDead = true;
    }
}
