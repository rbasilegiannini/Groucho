package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;
import com.personal.groucho.game.Spritesheet;

public class Spritesheets {

    public static Spritesheet grouchoIdle, grouchoWalk, grouchoShoot, grouchoAim,
            grouchoFire, grouchoDoor, grouchoDeath;

    public static Spritesheet skeletonIdle, skeletonWalk, skeletonHurt, skeletonDeath;

    public static int directions = 4;

    public static void load(Resources res){
        grouchoIdle = loadAnimation(res, R.drawable.groucho_idle, 1, directions);
        grouchoWalk = loadAnimation(res, R.drawable.groucho_walk, 7, directions);
        grouchoShoot = loadAnimation(res, R.drawable.groucho_shoot, 5, directions);
        grouchoAim = loadAnimation(res, R.drawable.groucho_aim, 1, directions);
        grouchoFire = loadAnimation(res, R.drawable.groucho_fire, 1, directions);
        grouchoDoor = loadAnimation(res, R.drawable.groucho_door, 6, directions);
        grouchoDeath = loadAnimation(res, R.drawable.groucho_death, 1, 1);

        skeletonIdle = loadAnimation(res, R.drawable.skeleton_idle, 1, directions);
        skeletonWalk = loadAnimation(res, R.drawable.skeleton_walk, 8, directions);
        skeletonHurt = loadAnimation(res, R.drawable.skeleton_hurt, 5, directions);
        skeletonDeath = loadAnimation(res, R.drawable.skeleton_death, 1, 1);
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
