package com.personal.groucho.game.assets;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.personal.groucho.R;
import com.personal.groucho.game.Spritesheet;

public class Spritesheets {

    public static Spritesheet grouchoIdle;
    public static Spritesheet grouchoWalk;
    public static Spritesheet grouchoShoot;
    public static Spritesheet grouchoAim;
    public static Spritesheet grouchoFire;
    public static Spritesheet grouchoDoor;
    public static Spritesheet grouchoDeath;

    public static Spritesheet skeletonIdle, skeletonWalk, skeletonHurt, skeletonDeath;
    public static Spritesheet zombieIdle, zombieWalk, zombieHurt, zombieDeath;
    public static Spritesheet wolfIdle, wolfWalk, wolfHurt, wolfDeath, wolfDeathWithoutKey;



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

        zombieIdle = loadAnimation(res, R.drawable.zombie_idle, 1, directions);
        zombieWalk = loadAnimation(res, R.drawable.zombie_walk, 8, directions);
        zombieHurt = loadAnimation(res, R.drawable.zombie_hurt, 5, directions);
        zombieDeath = loadAnimation(res, R.drawable.zombie_death, 1, 1);

        wolfIdle = loadAnimation(res, R.drawable.wolf_idle, 1, directions);
        wolfWalk = loadAnimation(res, R.drawable.wolf_walk, 8, directions);
        wolfHurt = loadAnimation(res, R.drawable.wolf_hurt, 5, directions);
        wolfDeath = loadAnimation(res, R.drawable.wolf_death, 1, 1);
        wolfDeathWithoutKey = loadAnimation(res, R.drawable.wolf_death_without_key, 1, 1);
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

    public static void release() {
        grouchoIdle.dispose();
        grouchoWalk.dispose();
        grouchoShoot.dispose();
        grouchoAim.dispose();
        grouchoFire.dispose();
        grouchoDoor.dispose();
        grouchoDeath.dispose();
        skeletonIdle.dispose();
        skeletonWalk.dispose();
        skeletonHurt.dispose();
        skeletonDeath.dispose();
        zombieIdle.dispose();
        zombieWalk.dispose();
        zombieHurt.dispose();
        zombieDeath.dispose();
        wolfIdle.dispose();
        wolfWalk.dispose();
        wolfHurt.dispose();
        wolfDeath.dispose();
        wolfDeathWithoutKey.dispose();
    }
}
