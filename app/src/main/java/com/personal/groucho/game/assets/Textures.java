package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Textures {
    public static Bitmap firstLevelFloor;
    public static Bitmap table;
    public static Bitmap wall, roof;
    public static Bitmap lightBulb;
    public static Bitmap health;

    public static void load(Resources res) {
        firstLevelFloor = setBitmap(res, R.drawable.wooden_floor,128, 128);
        table = setBitmap(res, R.drawable.table, 128, 124);
        wall = setBitmap(res, R.drawable.wall, 256, 256);
        roof = setBitmap(res, R.drawable.roof, 256, 256);
        lightBulb = setBitmap(res, R.drawable.lightbulb, 128, 128);
        health = setBitmap(res, R.drawable.health, 128, 128);
    }

    private static Bitmap setBitmap(Resources res, int id, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, id, options);
        bitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

        return bitmap;
    }
}
