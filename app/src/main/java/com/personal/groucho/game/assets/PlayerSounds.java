package com.personal.groucho.game.assets;

import com.personal.groucho.badlogic.androidgames.framework.Audio;
import com.personal.groucho.badlogic.androidgames.framework.Sound;

public class PlayerSounds {
    public static Sound loadingSound;
    public static Sound shootingSound;

    public static void init(Audio audio) {
        loadingSound = audio.newSound("loading.mp3");
        shootingSound = audio.newSound("shot.mp3");
    }
}
