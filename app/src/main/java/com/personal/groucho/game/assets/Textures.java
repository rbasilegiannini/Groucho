package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Textures {
    public static Bitmap firstLevelFloor;
    public static Bitmap table, littleTable;
    public static Bitmap bed;
    public static Bitmap dresser, dresserWithFlower;
    public static Bitmap wall;
    public static Bitmap windowInternal, windowNight;
    public static Bitmap brownDoor;
    public static Bitmap redCarpet, brownCarpet;
    public static Bitmap orangeCouch, littleLibrary;
    public static Bitmap lightBulb;
    public static Bitmap grouchoFrame;
    public static Bitmap grouchoWardrobe;
    public static Bitmap pause;
    public static Bitmap health;
    public static Bitmap grouchoBubble, dylanBubble;

    public static void load(Resources res) {
        firstLevelFloor = setBitmap(res, R.drawable.wooden_floor,64, 64);
        table = setBitmap(res, R.drawable.table, 128, 124);
        littleTable = setBitmap(res, R.drawable.little_table, 256, 385);
        bed = setBitmap(res, R.drawable.bed, 128, 128);
        dresser = setBitmap(res, R.drawable.dresser, 356, 505);
        dresserWithFlower = setBitmap(res, R.drawable.dresser_with_flowers, 356, 621);
        wall = setBitmap(res, R.drawable.wall, 256, 256);
        windowInternal = setBitmap(res, R.drawable.window_internal, 144, 212);
        windowNight = setBitmap(res, R.drawable.window_night, 144, 212);
        brownDoor = setBitmap(res, R.drawable.brown_door, 128, 256);
        redCarpet = setBitmap(res, R.drawable.red_carpet, 341, 512);
        brownCarpet = setBitmap(res, R.drawable.brown_carpet, 512, 356);
        orangeCouch = setBitmap(res, R.drawable.orange_couch, 512, 324);
        littleLibrary = setBitmap(res, R.drawable.little_library, 449, 417);
        lightBulb = setBitmap(res, R.drawable.lightbulb, 128, 128);
        grouchoFrame = setBitmap(res, R.drawable.groucho_frame, 256, 256);
        grouchoWardrobe = setBitmap(res, R.drawable.groucho_wardrobe, 256, 288);
        pause =  setBitmap(res, R.drawable.pause, 128, 128);
        health = setBitmap(res, R.drawable.health, 128, 128);
        grouchoBubble = setBitmap(res, R.drawable.groucho_bubble, 512, 256);
        dylanBubble = setBitmap(res, R.drawable.dylan_bubble, 512, 340);
    }

    private static Bitmap setBitmap(Resources res, int id, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, options);
        bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

        return bitmap;
    }

    public static void release() {
        releaseBitmap(firstLevelFloor);
        releaseBitmap(table);
        releaseBitmap(wall);
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
