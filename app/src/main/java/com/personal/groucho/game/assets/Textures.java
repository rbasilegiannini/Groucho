package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Textures {
    public static Bitmap firstLevelFloor;
    public static Bitmap table;
    public static Bitmap wall;
    public static Bitmap lightBulb;
    public static Bitmap pause;
    public static Bitmap health;
    public static Bitmap bubble;

    public static void load(Resources res) {
        firstLevelFloor = setBitmap(res, R.drawable.wooden_floor,64, 64);
        table = setBitmap(res, R.drawable.table, 128, 124);
        wall = setBitmap(res, R.drawable.wall, 256, 256);
        lightBulb = setBitmap(res, R.drawable.lightbulb, 128, 128);
        pause =  setBitmap(res, R.drawable.pause, 128, 128);
        health = setBitmap(res, R.drawable.health, 128, 128);
        bubble = setBitmap(res, R.drawable.bubble, 512, 256);
    }

    private static Bitmap setBitmap(Resources res, int id, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, options);
        bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

        return bitmap;
    }
}
