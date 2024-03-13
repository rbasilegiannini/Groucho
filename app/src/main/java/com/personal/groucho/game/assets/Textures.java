package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Textures {
    public static Bitmap woodFloor, orangeFloor, lightWoodFloor, brownFloor, bathroomFloor, grassFloor;
    public static Bitmap table, littleTable, mediumTable;
    public static Bitmap chairLeft, chairRight, chairUp, chairDown;
    public static Bitmap armChairLeft, armChairRight;
    public static Bitmap bed, littleGrass, ironingBoard, statue, statueWithKey, statueBottom;
    public static Bitmap dresser, dresserRight;
    public static Bitmap woodWall, orangeWall, greenWall, bathroomWall, whiteWall, stoneWall, stoneWall2;
    public static Bitmap windowInternal, windowNight;
    public static Bitmap brownDoor, heavyDoor, heavyDoorOpened;
    public static Bitmap redCarpet, brownCarpet, littleCarpetBathroom, sink;
    public static Bitmap orangeCouch, littleLibrary, library, greenCouchRight;
    public static Bitmap hanger, littleCarpet, littleGreenCarpet, littleGreenCarpetVer, lamp;
    public static Bitmap littleDresser, littleDresserWithKey, bench, kitchen, fridge, bigTable;
    public static Bitmap wc, box, tableWithFlowers, headstone, stone;
    public static Bitmap lightBulb;
    public static Bitmap grouchoFrame, meFrame;
    public static Bitmap grouchoWardrobe;
    public static Bitmap pause;
    public static Bitmap health;
    public static Bitmap grouchoBubble, dylanBubble;

    public static void load(Resources res) {
        loadFloors(res);
        loadWalls(res);
        loadFurniture(res);
        loadGrouchoRes(res);
        loadDoors(res);
        loadCarpets(res);
        loadWindows(res);
        loadHUD(res);
    }

    private static void loadFloors(Resources res) {
        bathroomFloor = setBitmap(res, R.drawable.bathroom_floor, 64, 64);
        woodFloor = setBitmap(res, R.drawable.wood_floor, 32, 32);
        orangeFloor = setBitmap(res, R.drawable.orange_floor, 32, 32);
        lightWoodFloor = setBitmap(res, R.drawable.light_wood_floor, 64, 64);
        brownFloor = setBitmap(res, R.drawable.brown_floor, 32, 32);
        grassFloor = setBitmap(res, R.drawable.grass_floor_big, 256, 256);
    }

    private static void loadWalls(Resources res) {
        orangeWall = setBitmap(res, R.drawable.orange_internal_wall, 32, 32);
        whiteWall = setBitmap(res, R.drawable.white_wall, 64, 64);
        woodWall = setBitmap(res, R.drawable.wood_wall, 64, 64);
        greenWall = setBitmap(res, R.drawable.green_wall, 62, 64);
        bathroomWall = setBitmap(res, R.drawable.bathroom_wall, 64, 64);
        stoneWall = setBitmap(res, R.drawable.stone_wall, 64, 64);
        stoneWall2 = setBitmap(res, R.drawable.stone_wall_external, 64, 64);
    }

    private static void loadFurniture(Resources res) {
        bigTable = setBitmap(res, R.drawable.big_table, 30, 46);
        fridge = setBitmap(res, R.drawable.fridge, 26, 55);
        kitchen = setBitmap(res, R.drawable.kitchen, 112, 63);
        bench = setBitmap(res, R.drawable.bench, 27, 61);
        stone = setBitmap(res, R.drawable.stone_medium, 29, 28);
        headstone = setBitmap(res, R.drawable.headstone, 26,40);
        statue = setBitmap(res, R.drawable.statue, 37, 72);
        statueWithKey = setBitmap(res, R.drawable.statue_with_key, 37, 72);
        statueBottom = setBitmap(res, R.drawable.statue_bottom, 97, 74);
        health = setBitmap(res, R.drawable.health, 128, 128);
        littleGrass = setBitmap(res, R.drawable.little_grass, 10, 23);
        sink = setBitmap(res, R.drawable.sink, 42, 56);
        wc = setBitmap(res, R.drawable.wc, 43, 32);
        meFrame = setBitmap(res, R.drawable.me_frame, 64, 64);
        orangeCouch = setBitmap(res, R.drawable.orange_couch, 49, 31);
        littleTable = setBitmap(res, R.drawable.little_table, 16, 24);
        table = setBitmap(res, R.drawable.table, 64, 62);
        tableWithFlowers = setBitmap(res, R.drawable.table_with_flowers, 24, 23);
        mediumTable = setBitmap(res, R.drawable.medium_table, 24, 19);
        armChairLeft = setBitmap(res, R.drawable.armchair_left, 23, 36);
        armChairRight = setBitmap(res, R.drawable.armchair_right, 23, 36);
        bed = setBitmap(res, R.drawable.bed, 64, 64);
        box = setBitmap(res, R.drawable.box, 16, 16);
        chairDown = setBitmap(res, R.drawable.chair_down, 16, 21);
        chairLeft = setBitmap(res, R.drawable.chair_left, 15, 28);
        chairRight = setBitmap(res, R.drawable.chair_right, 15, 28);
        chairUp = setBitmap(res, R.drawable.chair_up, 16, 27);
        dresser = setBitmap(res, R.drawable.dresser, 25, 32);
        dresserRight = setBitmap(res, R.drawable.dresser_right, 16, 48);
        greenCouchRight = setBitmap(res, R.drawable.green_couch_right, 22, 55);
        hanger = setBitmap(res, R.drawable.hanger, 16, 44);
        ironingBoard = setBitmap(res, R.drawable.ironing_board, 25, 22);
        lamp = setBitmap(res, R.drawable.lamp, 15, 46);
        library = setBitmap(res, R.drawable.library, 46, 45);
        littleLibrary = setBitmap(res, R.drawable.little_library, 30, 26);
        littleDresser = setBitmap(res, R.drawable.little_dresser, 16, 22);
        littleDresserWithKey = setBitmap(res, R.drawable.little_dresser_with_key, 16, 22);
    }

    private static void loadGrouchoRes(Resources res) {
        grouchoBubble = setBitmap(res, R.drawable.groucho_bubble, 512, 256);
        grouchoFrame = setBitmap(res, R.drawable.groucho_frame, 64, 64);
        grouchoWardrobe = setBitmap(res, R.drawable.groucho_wardrobe, 64, 72);
        dylanBubble = setBitmap(res, R.drawable.dylan_bubble, 512, 340);
    }

    private static void loadDoors(Resources res) {
        heavyDoor = setBitmap(res, R.drawable.heavy_door, 128, 163);
        heavyDoorOpened = setBitmap(res, R.drawable.heavy_door_opened, 128, 163);
        brownDoor = setBitmap(res, R.drawable.brown_door, 26, 46);
    }

    private static void loadCarpets (Resources res){
        brownCarpet = setBitmap(res, R.drawable.brown_carpet, 46, 32);
        littleCarpet = setBitmap(res, R.drawable.little_carpet, 15, 31);
        littleCarpetBathroom = setBitmap(res, R.drawable.little_carpet_bathroom, 31, 15);
        littleGreenCarpet = setBitmap(res, R.drawable.green_little_carpet, 22, 12);
        littleGreenCarpetVer = setBitmap(res, R.drawable.green_little_carpet_ver, 12, 22);
        redCarpet = setBitmap(res, R.drawable.red_carpet, 92, 61);
    }

    private static void loadWindows (Resources res){
        windowInternal = setBitmap(res, R.drawable.window_internal, 25, 28);
        windowNight = setBitmap(res, R.drawable.window_night, 25, 28);
    }

    private static void loadHUD (Resources res){
        lightBulb = setBitmap(res, R.drawable.lightbulb, 64, 64);
        pause = setBitmap(res, R.drawable.pause, 128, 128);
    }

    private static Bitmap setBitmap(Resources res, int id, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, options);
        bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

        return bitmap;
    }

    public static void release() {
        releaseBitmap(woodFloor);
        releaseBitmap(orangeFloor);
        releaseBitmap(lightWoodFloor);
        releaseBitmap(brownFloor);
        releaseBitmap(bathroomFloor);
        releaseBitmap(grassFloor);
        releaseBitmap(table);
        releaseBitmap(littleTable);
        releaseBitmap(mediumTable);
        releaseBitmap(chairLeft);
        releaseBitmap(chairRight);
        releaseBitmap(chairUp);
        releaseBitmap(chairDown);
        releaseBitmap(armChairLeft);
        releaseBitmap(armChairRight);
        releaseBitmap(bed);
        releaseBitmap(littleGrass);
        releaseBitmap(ironingBoard);
        releaseBitmap(statue);
        releaseBitmap(statueWithKey);
        releaseBitmap(statueBottom);
        releaseBitmap(dresser);
        releaseBitmap(dresserRight);
        releaseBitmap(woodWall);
        releaseBitmap(orangeWall);
        releaseBitmap(greenWall);
        releaseBitmap(bathroomWall);
        releaseBitmap(whiteWall);
        releaseBitmap(stoneWall);
        releaseBitmap(stoneWall2);
        releaseBitmap(windowInternal);
        releaseBitmap(windowNight);
        releaseBitmap(brownDoor);
        releaseBitmap(heavyDoor);
        releaseBitmap(heavyDoorOpened);
        releaseBitmap(redCarpet);
        releaseBitmap(brownCarpet);
        releaseBitmap(littleCarpetBathroom);
        releaseBitmap(sink);
        releaseBitmap(orangeCouch);
        releaseBitmap(littleLibrary);
        releaseBitmap(library);
        releaseBitmap(greenCouchRight);
        releaseBitmap(hanger);
        releaseBitmap(littleCarpet);
        releaseBitmap(littleGreenCarpet);
        releaseBitmap(littleGreenCarpetVer);
        releaseBitmap(lamp);
        releaseBitmap(littleDresser);
        releaseBitmap(littleDresserWithKey);
        releaseBitmap(bench);
        releaseBitmap(kitchen);
        releaseBitmap(fridge);
        releaseBitmap(bigTable);
        releaseBitmap(wc);
        releaseBitmap(box);
        releaseBitmap(tableWithFlowers);
        releaseBitmap(headstone);
        releaseBitmap(stone);
        releaseBitmap(lightBulb);
        releaseBitmap(grouchoFrame);
        releaseBitmap(meFrame);
        releaseBitmap(grouchoWardrobe);
        releaseBitmap(pause);
    }

    private static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
