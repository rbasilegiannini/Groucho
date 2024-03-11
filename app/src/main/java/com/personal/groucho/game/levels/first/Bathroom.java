package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.CharacterFactory.getZombie;
import static com.personal.groucho.game.assets.Textures.bathroomFloor;
import static com.personal.groucho.game.assets.Textures.bathroomWall;
import static com.personal.groucho.game.assets.Textures.box;
import static com.personal.groucho.game.assets.Textures.chairLeft;
import static com.personal.groucho.game.assets.Textures.dresserRight;
import static com.personal.groucho.game.assets.Textures.ironingBoard;
import static com.personal.groucho.game.assets.Textures.lamp;
import static com.personal.groucho.game.assets.Textures.littleCarpetBathroom;
import static com.personal.groucho.game.assets.Textures.littleDresserWithKey;
import static com.personal.groucho.game.assets.Textures.littleGrass;
import static com.personal.groucho.game.assets.Textures.littleGreenCarpetVer;
import static com.personal.groucho.game.assets.Textures.littleTable;
import static com.personal.groucho.game.assets.Textures.sink;
import static com.personal.groucho.game.assets.Textures.tableWithFlowers;
import static com.personal.groucho.game.assets.Textures.wc;
import static com.personal.groucho.game.assets.Textures.windowNight;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.controller.states.StateName.IDLE;
import static com.personal.groucho.game.controller.states.StateName.PATROL;
import static com.personal.groucho.game.levels.first.BathroomEvents.dresserWithKeyEvent;
import static com.personal.groucho.game.levels.first.BathroomEvents.entryHallDoorEvent;
import static com.personal.groucho.game.levels.first.BathroomEvents.firstTimeInRoomEvent;
import static com.personal.groucho.game.levels.first.BathroomEvents.talkEvent;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.levels.Room;

public class Bathroom extends Room {
    protected final FirstLevel level;
    protected int playerPosX, playerPosY;
    protected GameObject bathroomKey;

    public Bathroom(GameWorld gameWorld, FirstLevel level) {
        super(3000, 1200, gameWorld);
        this.internalWall = bathroomWall;
        this.externalWall = woodWall;
        this.level = level;

        // Set floor
        setFloor(bathroomFloor, 128, 128);
    }

    @Override
    public void init(){
        super.init();
        playerPosX = (int)(1.1*cellSize);
        playerPosY = 3*cellSize;

        if (firstTime) {
            firstTimeInRoomEvent(this);
        }

        makeWalls();
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
        gameWorld.player.setOrientation(RIGHT);
        setBrightness(minBrightness);
    }

    private void makeWalls() {
        int wallCenterY = (int)(-0.5 * unit);
        int wallDim = 6 * unit;
        for (int i = 2; i < 8; i++) {
            addWall((int) (i*2 * unit + 0.5*unit), wallCenterY, wallDim);
        }
    }

    private void makeFurniture() {
        addDynamicFurn(5*unit, 5*unit, 90, 150, 5f, chairLeft);
        addDynamicFurn((int)(3.9*unit), 2*unit, 90, 300, 35f, dresserRight);
        addDynamicFurn((int)(0.5*unit), 5*unit, 150, 150, 5f, littleTable);
        addDynamicFurn((int)(7.7*unit), 4*unit, 150, 130, 5f, ironingBoard);
        addDynamicFurn((int)(10.7*unit), 5*unit, 150, 180, 15f, tableWithFlowers);
        addDynamicFurn((int)(16.7*unit), 5*unit, 100, 100, 5f, box);
        addDynamicFurn((int)(17.3*unit), 5*unit, 70, 70, 5f, box);
        addDynamicFurn((int)(17.0*unit), (int)(4.5*unit), 110, 110, 5f, box);
        addDynamicFurn((int)(17.7*unit), (int)(2*unit), 90, 300, 35f, dresserRight);
    }

    private void makeDecorations() {
        addWallDec((int)(0.85*unit), 0, 70, 140, littleGrass);
        addWallDec((int)(3.5*unit), 0, 70, 140, littleGrass);
        addWallDec((int)(5.5* unit), 0, 250, 250, wc);
        addWallDec((int)(7.5* unit), 0, 250, 250, wc);
        addWallDec((int)(9.5* unit), 0, 250, 250, wc);
        addWallDec((int)(11.5* unit), 0, 250, 250, wc);
        addWallDec((int)(13.5* unit), 0, 250, 250, wc);
        addWallDec((int)(2.2* unit), -unit/2, 250, 380, sink);
        addWallDec((int)(15.2*unit), (int)(-0.5*unit), 100, 356, lamp);
        addFloorDec((int)(2.2* unit), unit, 250, 100, littleCarpetBathroom);
    }

    private void makeTriggers() {
        addWallTrigger((int)(16.3*unit), -unit, (int)(16.0*unit), (int)(-0.75*unit),
                188, 200, windowNight, () -> talkEvent(this, R.string.groucho_entryhall_window));
        bathroomKey = addWallTrigger((int)(17.3*unit), 0, (int)(17.3*unit), -unit/4,
                100, 100, littleDresserWithKey, () -> dresserWithKeyEvent(this));
        addFloorTrigger((int)(0.35*unit), 3*unit, (int)(-0.35*unit), 3*unit,
                90, 150, littleGreenCarpetVer, ()->entryHallDoorEvent(this));
    }

    private void makeEnemies() {
        addEnemy((int)(5.5*unit), unit, DOWN, getZombie(), PATROL);
        addEnemy((int)(7.5*unit), unit, DOWN, getZombie(), IDLE);
        addEnemy((int)(11.5*unit), (int)(2.5*unit), UP, getZombie(), PATROL);
        addEnemy((int)(13.5*unit), (int)(3.3*unit), UP, getZombie(), IDLE);
    }
}
