package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Textures.brownCarpet;
import static com.personal.groucho.game.assets.Textures.brownDoor;
import static com.personal.groucho.game.assets.Textures.dresser;
import static com.personal.groucho.game.assets.Textures.littleGreenCarpet;
import static com.personal.groucho.game.assets.Textures.littleLibrary;
import static com.personal.groucho.game.assets.Textures.littleTable;
import static com.personal.groucho.game.assets.Textures.orangeCouch;
import static com.personal.groucho.game.assets.Textures.orangeWall;
import static com.personal.groucho.game.assets.Textures.windowNight;
import static com.personal.groucho.game.assets.Textures.woodFloor;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.levels.first.HallwayEvents.firstTimeInRoomEvent;
import static com.personal.groucho.game.levels.first.HallwayEvents.fromGrouchoRoomEvent;
import static com.personal.groucho.game.levels.first.HallwayEvents.fromLibraryEvent;
import static com.personal.groucho.game.levels.first.HallwayEvents.grouchoRoomDoorEvent;
import static com.personal.groucho.game.levels.first.HallwayEvents.libraryDoorEvent;
import static com.personal.groucho.game.levels.first.HallwayEvents.talkEvent;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.levels.Room;

public class Hallway extends Room {
    public static boolean firstTime = true;
    protected final FirstLevel level;
    protected int playerPosX, playerPosY;
    protected Orientation playerOrientation = UP;

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

        if (level.fromGrouchoRoomToHallway) {
            fromGrouchoRoomEvent(this);
        }
        else if (level.fromLibraryToHallway) {
            fromLibraryEvent(this);
        }

        if (firstTime) {
            firstTimeInRoomEvent(this);
        }

        makeTriggers();
        makeDecorations();
        makeFurniture();

        allocateRoom();
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
        addDynamicFurn(6*unit,  (int)(0.75*unit), 150, 150, 5f, littleTable);
    }

    private void makeTriggers() {
        addWallTrigger((int)(2.5*unit), (int)(3.1*unit), (int) (2.5*unit), (int) (2.95*unit),
                160, 280, brownDoor, () -> grouchoRoomDoorEvent(this));
        addWallTrigger((int)(10.5*unit), (int)(-0.85*unit), (int)(10.5*unit), (int)(-0.95*unit),
                160, 280, brownDoor, () -> libraryDoorEvent(this));
        addWallTrigger((int)(8.5*unit), (int)(-0.25*unit), (int)(8.5*unit), (int)(-0.5*unit),
                180, 128, littleLibrary, ()-> talkEvent(this, R.string.groucho_hallway_library));
        addWallTrigger((int)(2.5*unit), -unit, (int)(2.5*unit), (int)(-0.75*unit),
                188, 200, windowNight, () -> talkEvent(this, R.string.groucho_hallway_window));
    }

    private void makeDecorations() {
        addFloorDec((int)(10.5*unit), (int)(0.4*unit), 170, 90, littleGreenCarpet);
        addFloorDec(6*unit, (int)(1.5*unit), 512, 356, brownCarpet);
        addWallDec(6*unit, 0, 400, 210, orangeCouch);
        addWallDec(unit, (int)(-0.25*unit), 180, 250, dresser);
        addWallDec((int)(7.5*unit), 3*unit, 188, 200, windowNight);
        addWallDec((int)(10.5*unit), 3*unit, 188, 200, windowNight);
    }
}
