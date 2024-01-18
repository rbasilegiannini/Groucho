package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Textures {
    public static Bitmap firstLevelFloor;

    public static void load(Resources res) {
        int targetWidth = 512;
        int targetHeight = 512;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        firstLevelFloor = BitmapFactory.decodeResource(res, R.drawable.wooden_floor, options);
        firstLevelFloor = Bitmap.createScaledBitmap(firstLevelFloor, targetWidth, targetHeight, true);
    }
}
