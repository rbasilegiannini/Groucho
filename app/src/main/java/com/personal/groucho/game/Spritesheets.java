package com.personal.groucho.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Spritesheets {

    public static Spritesheet groucho_walk;
    public static Spritesheet groucho_shoot;
    public static Spritesheet groucho_door;
    public static Spritesheet groucho_death;

    public static void load(Resources res){
        groucho_walk = loadGroucho(res, R.drawable.groucho_walk, 9);
        groucho_shoot = loadGroucho(res, R.drawable.groucho_shoot, 5);
        groucho_door = loadGroucho(res, R.drawable.groucho_door, 6);
        groucho_death = loadGroucho(res, R.drawable.groucho_death, 6);
    }

    private static Spritesheet loadGroucho(Resources res, int idDrawable, int numOfFrames) {
        int grouchoAnimations = 4;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(res, idDrawable, options);

        Spritesheet spritesheet = new Spritesheet(bitmap, grouchoAnimations);

        spritesheet.setFrameSize(64, 64);
        for (int anim = 0; anim < grouchoAnimations; anim++) {
            spritesheet.setAnimation(anim, anim*numOfFrames, numOfFrames, 300);
        }

        return spritesheet;
    }
}
