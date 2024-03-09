package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Textures.bed;
import static com.personal.groucho.game.assets.Textures.brownDoor;
import static com.personal.groucho.game.assets.Textures.brownFloor;
import static com.personal.groucho.game.assets.Textures.grouchoFrame;
import static com.personal.groucho.game.assets.Textures.grouchoWardrobe;
import static com.personal.groucho.game.assets.Textures.littleGreenCarpet;
import static com.personal.groucho.game.assets.Textures.orangeWall;
import static com.personal.groucho.game.assets.Textures.redCarpet;
import static com.personal.groucho.game.assets.Textures.table;
import static com.personal.groucho.game.assets.Textures.windowInternal;
import static com.personal.groucho.game.assets.Textures.woodWall;
import static com.personal.groucho.game.constants.Environment.maxBrightness;
import static com.personal.groucho.game.levels.first.GrouchoRoomEvents.firstTimeInRoomEvent;
import static com.personal.groucho.game.levels.first.GrouchoRoomEvents.hallwayDoorEvent;
import static com.personal.groucho.game.levels.first.GrouchoRoomEvents.talkEvent;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.levels.Room;

public class GrouchoRoom extends Room {
    public static boolean firstTime = true;
    protected final FirstLevel level;
    protected int playerPosX, playerPosY;

    public GrouchoRoom(GameWorld gameWorld, FirstLevel level) {
        super(1000, 1000, gameWorld);
        this.internalWall = orangeWall;
        this.externalWall = woodWall;
        this.level = level;

        // Set floor
        Bitmap floor = Bitmap.createScaledBitmap(brownFloor, 128, 128, false);
        BitmapShader bs = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        floorPaint.setShader(bs);
    }

    @Override
    public void init() {
        super.init();

        if (firstTime) {
            firstTimeInRoomEvent(this);
        }
        else {
            playerPosX = 2*unit;
            playerPosY = unit;
        }

        makeFurniture();
        makeTriggers();
        makeDecorations();

        allocateRoom();
    }

    @Override
    public void allocateRoom(){
        super.allocateRoom();
        gameWorld.player.setPos(playerPosX, playerPosY);
        setBrightness(maxBrightness);
    }

    private void makeTriggers() {
        addWallTrigger((int)(0.75*unit), (int)(-unit), (int)(0.75*unit), (int)(-0.5*unit),
                128, 128, grouchoFrame, () -> talkEvent(this, R.string.groucho_bedroom_photo));
        addWallTrigger((int)(4.5*unit), (int)(-0.5*unit), (int) (4.5*unit), (int) (-1.2*unit),
                280, 350, grouchoWardrobe, () -> talkEvent(this, R.string.groucho_bedroom_wardrobe));
        addWallTrigger(2*unit, (int)(-0.85*unit), 2*unit, (int) (-0.95*unit),
                160, 280, brownDoor, () -> hallwayDoorEvent(this));
    }

    private void makeFurniture() {
        addDynamicFurn(5*unit, (int)(3.5*unit), 250, 150, 5f, table);
        addDynamicFurn(5*unit, 2*unit, 280, 350, 100f, bed);
    }

    private void makeDecorations() {
        addFloorDec((int)(2*unit), (int)(0.4*unit), 170, 90, littleGreenCarpet);
        addFloorDec(2*unit, (int)(2.5*unit), 512, 380, redCarpet);
        addWallDec((int)(1.5*unit), 6*unit, 188, 200, windowInternal);
        addWallDec((int)(4.5*unit), 6*unit, 188, 200, windowInternal);
    }
}
