package com.personal.groucho.game.animation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;

public class Spritesheets {

    public static Spritesheet groucho_idle, groucho_walk, groucho_shoot, groucho_aim,
            groucho_fire, groucho_door, groucho_death;

    public static Spritesheet skeleton_idle, skeleton_walk, skeleton_hurt, skeleton_death;

    public static void load(Resources res){
//        groucho_idle = loadGroucho(res, R.drawable.groucho_idle, 1);
//        groucho_walk = loadGroucho(res, R.drawable.groucho_walk, 8);
//        groucho_shoot = loadGroucho(res, R.drawable.groucho_shoot, 5);
//        groucho_aim = loadGroucho(res, R.drawable.groucho_aim, 1);
//        groucho_fire = loadGroucho(res, R.drawable.groucho_fire, 1);
//        groucho_door = loadGroucho(res, R.drawable.groucho_door, 6);
//        groucho_death = loadGroucho(res, R.drawable.groucho_death, 6);
        int directions = 4;
        groucho_idle = loadAnimation(res, R.drawable.groucho_idle, 1, directions);
        groucho_walk = loadAnimation(res, R.drawable.groucho_walk, 8, directions);
        groucho_shoot = loadAnimation(res, R.drawable.groucho_shoot, 5, directions);
        groucho_aim = loadAnimation(res, R.drawable.groucho_aim, 1, directions);
        groucho_fire = loadAnimation(res, R.drawable.groucho_fire, 1, directions);
        groucho_door = loadAnimation(res, R.drawable.groucho_door, 6, directions);
        groucho_death = loadAnimation(res, R.drawable.groucho_death, 6, 1);

        skeleton_idle = loadAnimation(res, R.drawable.skeleton_idle, 1, directions);
        skeleton_walk = loadAnimation(res, R.drawable.skeleton_walk, 9, directions);
        skeleton_hurt = loadAnimation(res, R.drawable.skeleton_hurt, 6, directions);
        skeleton_death = loadAnimation(res, R.drawable.skeleton_death, 6, 1);
    }

    private static Spritesheet loadGroucho(Resources res, int idDrawable, int numOfFrames) {
        int grouchoAnimations = 4;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(res, idDrawable, options);

        Spritesheet spritesheet = new Spritesheet(bitmap, grouchoAnimations);

        spritesheet.setFrameSize(64, 64);
        for (int anim = 0; anim < grouchoAnimations; anim++) {
            spritesheet.setAnimation(anim, anim*numOfFrames, numOfFrames, 100);
        }

        return spritesheet;
    }

    private static Spritesheet loadAnimation(Resources res, int idDraw, int frames, int animations) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(res, idDraw, options);

        Spritesheet spritesheet = new Spritesheet(bitmap, animations);

        spritesheet.setFrameSize(64, 64);
        for (int anim = 0; anim < animations; anim++) {
            spritesheet.setAnimation(anim, anim*frames, frames, 100);
        }

        return spritesheet;
    }
}
