package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Textures {
    public static Bitmap woodFloor, orangeFloor, lightWoodFloor, brownFloor, bathroomFloor;
    public static Bitmap table, littleTable, mediumTable;
    public static Bitmap chairLeft, chairRight, chairUp, chairDown;
    public static Bitmap armChairLeft, armChairRight;
    public static Bitmap bed, littleGrass, ironingBoard;
    public static Bitmap dresser, dresserRight;
    public static Bitmap woodWall, orangeWall, greenWall, bathroomWall, whiteWall;
    public static Bitmap windowInternal, windowNight;
    public static Bitmap brownDoor, heavyDoor;
    public static Bitmap redCarpet, brownCarpet, littleCarpetBathroom, sink;
    public static Bitmap orangeCouch, littleLibrary, library, greenCouchRight;
    public static Bitmap hanger, littleCarpet, littleGreenCarpet, littleGreenCarpetVer, lamp;
    public static Bitmap littleDresser, littleDresserWithKey;
    public static Bitmap wc, box, tableWithFlowers;
    public static Bitmap lightBulb;
    public static Bitmap grouchoFrame, meFrame;
    public static Bitmap grouchoWardrobe;
    public static Bitmap pause;
    public static Bitmap health;
    public static Bitmap grouchoBubble, dylanBubble;

    public static void load(Resources res) {
        armChairLeft = setBitmap(res, R.drawable.armchair_left, 74, 114);
        armChairRight = setBitmap(res, R.drawable.armchair_right, 74, 114);
        bathroomFloor = setBitmap(res, R.drawable.bathroom_floor, 64, 64);
        bathroomWall = setBitmap(res, R.drawable.bathroom_wall, 64, 64);
        bed = setBitmap(res, R.drawable.bed, 64, 64);
        brownCarpet = setBitmap(res, R.drawable.brown_carpet, 64, 44);
        brownDoor = setBitmap(res, R.drawable.brown_door, 41, 72);
        brownFloor = setBitmap(res, R.drawable.brown_floor, 64, 64);
        box = setBitmap(res, R.drawable.box, 52, 52);
        chairDown = setBitmap(res, R.drawable.chair_down, 66, 90);
        chairLeft = setBitmap(res, R.drawable.chair_left, 50, 95);
        chairRight = setBitmap(res, R.drawable.chair_right, 50, 95);
        chairUp = setBitmap(res, R.drawable.chair_up, 66, 118);
        dylanBubble = setBitmap(res, R.drawable.dylan_bubble, 512, 340);
        dresser = setBitmap(res, R.drawable.dresser, 64, 85);
        dresserRight = setBitmap(res, R.drawable.dresser_right, 38, 109);
        greenCouchRight = setBitmap(res, R.drawable.green_couch_right, 50, 125);
        greenWall = setBitmap(res, R.drawable.green_wall, 64, 64);
        grouchoBubble = setBitmap(res, R.drawable.groucho_bubble, 512, 256);
        grouchoFrame = setBitmap(res, R.drawable.groucho_frame, 64, 64);
        grouchoWardrobe = setBitmap(res, R.drawable.groucho_wardrobe, 96, 108);
        hanger = setBitmap(res, R.drawable.hanger, 46, 125);
        health = setBitmap(res, R.drawable.health, 128, 128);
        heavyDoor = setBitmap(res, R.drawable.heavy_door, 128, 163);
        ironingBoard = setBitmap(res, R.drawable.ironing_board, 90, 79);
        lamp = setBitmap(res, R.drawable.lamp, 61, 188);
        library = setBitmap(res, R.drawable.library, 64, 64);
        lightBulb = setBitmap(res, R.drawable.lightbulb, 64, 64);
        littleLibrary = setBitmap(res, R.drawable.little_library, 60, 52);
        littleTable = setBitmap(res, R.drawable.little_table, 50, 75);
        littleDresser = setBitmap(res, R.drawable.little_dresser, 45, 62);
        littleDresserWithKey = setBitmap(res, R.drawable.little_dresser_with_key, 45, 62);
        lightWoodFloor = setBitmap(res, R.drawable.light_wood_floor, 64, 64);
        littleCarpet = setBitmap(res, R.drawable.little_carpet, 44, 98);
        littleCarpetBathroom = setBitmap(res, R.drawable.little_carpet_bathroom, 98, 44);
        littleGreenCarpet = setBitmap(res, R.drawable.green_little_carpet, 73, 40);
        littleGreenCarpetVer = setBitmap(res, R.drawable.green_little_carpet_ver, 40, 73);
        littleGrass = setBitmap(res, R.drawable.little_grass, 30, 69);
        mediumTable = setBitmap(res, R.drawable.medium_table, 89, 70);
        meFrame = setBitmap(res, R.drawable.me_frame, 64, 64);
        orangeCouch = setBitmap(res, R.drawable.orange_couch, 99, 63);
        orangeFloor = setBitmap(res, R.drawable.orange_floor, 64, 64);
        orangeWall = setBitmap(res, R.drawable.orange_internal_wall, 64, 64);
        pause = setBitmap(res, R.drawable.pause, 128, 128);
        redCarpet = setBitmap(res, R.drawable.red_carpet, 128, 85);
        sink = setBitmap(res, R.drawable.sink, 105, 133);
        table = setBitmap(res, R.drawable.table, 95, 92);
        tableWithFlowers = setBitmap(res, R.drawable.table_with_flowers, 91, 90);
        wc = setBitmap(res, R.drawable.wc, 231, 154);
        whiteWall = setBitmap(res, R.drawable.white_wall, 64, 64);
        windowInternal = setBitmap(res, R.drawable.window_internal, 90, 101);
        windowNight = setBitmap(res, R.drawable.window_night, 90, 101);
        woodFloor = setBitmap(res, R.drawable.wood_floor, 32, 32);
        woodWall = setBitmap(res, R.drawable.wood_wall, 64, 64);
    }

    private static Bitmap setBitmap(Resources res, int id, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, options);
        bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

        return bitmap;
    }

    // TODO: Update release
    public static void release() {
        releaseBitmap(woodFloor);
        releaseBitmap(table);
        releaseBitmap(woodWall);
        releaseBitmap(lightBulb);
        releaseBitmap(pause);
        releaseBitmap(health);
        releaseBitmap(grouchoBubble);
    }

    private static void releaseBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
