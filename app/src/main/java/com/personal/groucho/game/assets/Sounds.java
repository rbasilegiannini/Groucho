package com.personal.groucho.game.assets;

import com.personal.groucho.badlogic.androidgames.framework.Audio;
import com.personal.groucho.badlogic.androidgames.framework.Sound;

public class Sounds {
    public static Sound loading;
    public static Sound shooting;
    public static Sound stabbing;
    public static Sound click;
    public static Sound bulletHitFurniture;
    public static Sound bulletHitEnemy;
    public static Sound bodyHitFurniture;
    public static Sound healing;
    public static Sound door, key;
    public static Sound throwing;
    public static Sound wolf, zombie, skeleton;

    public static void init(Audio audio) {
        loading = audio.newSound("loading.mp3");
        shooting = audio.newSound("shot.mp3");
        stabbing = audio.newSound("stab.mp3");
        click = audio.newSound("click.mp3");
        bulletHitEnemy = audio.newSound("bullet_hit_enemy.mp3");
        bulletHitFurniture = audio.newSound("bullet_hit_furniture.mp3");
        bodyHitFurniture = audio.newSound("body_hit_furniture.mp3");
        healing = audio.newSound("healing.mp3");
        door = audio.newSound("door.mp3");
        throwing = audio.newSound("throw.mp3");
        wolf = audio.newSound("wolf.mp3");
        zombie = audio.newSound("zombie.mp3");
        skeleton = audio.newSound("skeleton.mp3");
        key = audio.newSound("key.mp3");
    }

    public static void release() {
        releaseSound(loading);
        releaseSound(shooting);
        releaseSound(stabbing);
        releaseSound(click);
        releaseSound(bulletHitEnemy);
        releaseSound(bulletHitFurniture);
        releaseSound(bodyHitFurniture);
        releaseSound(healing);
        releaseSound(door);
        releaseSound(throwing);
        releaseSound(wolf);
        releaseSound(zombie);
        releaseSound(skeleton);
        releaseSound(key);
    }

    private static void releaseSound(Sound sound) {
        if (sound != null) {
            sound.dispose();
        }
    }
}
