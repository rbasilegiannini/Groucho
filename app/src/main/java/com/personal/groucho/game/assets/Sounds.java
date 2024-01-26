package com.personal.groucho.game.assets;

import com.personal.groucho.badlogic.androidgames.framework.Audio;
import com.personal.groucho.badlogic.androidgames.framework.Sound;

public class Sounds {
    public static Sound loadingSound;
    public static Sound shootingSound;
    public static Sound click;
    public static Sound bulletHitFurniture;
    public static Sound bulletHitEnemy;

    public static void init(Audio audio) {
        loadingSound = audio.newSound("loading.mp3");
        shootingSound = audio.newSound("shot.mp3");
        click = audio.newSound("click.mp3");
        bulletHitEnemy = audio.newSound("bullet_hit_enemy.mp3");
        bulletHitFurniture = audio.newSound("bullet_hit_furniture.mp3");
    }
}
