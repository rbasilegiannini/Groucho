package com.personal.groucho.game;

import static com.personal.groucho.game.assets.Spritesheets.grouchoAim;
import static com.personal.groucho.game.assets.Spritesheets.grouchoDeath;
import static com.personal.groucho.game.assets.Spritesheets.grouchoFire;
import static com.personal.groucho.game.assets.Spritesheets.grouchoIdle;
import static com.personal.groucho.game.assets.Spritesheets.grouchoWalk;
import static com.personal.groucho.game.assets.Spritesheets.skeletonDeath;
import static com.personal.groucho.game.assets.Spritesheets.skeletonHurt;
import static com.personal.groucho.game.assets.Spritesheets.skeletonIdle;
import static com.personal.groucho.game.assets.Spritesheets.skeletonWalk;
import static com.personal.groucho.game.assets.Spritesheets.wolfDeath;
import static com.personal.groucho.game.assets.Spritesheets.wolfHurt;
import static com.personal.groucho.game.assets.Spritesheets.wolfIdle;
import static com.personal.groucho.game.assets.Spritesheets.wolfWalk;
import static com.personal.groucho.game.assets.Spritesheets.zombieDeath;
import static com.personal.groucho.game.assets.Spritesheets.zombieHurt;
import static com.personal.groucho.game.assets.Spritesheets.zombieIdle;
import static com.personal.groucho.game.assets.Spritesheets.zombieWalk;
import static com.personal.groucho.game.constants.Character.grouchoHealth;
import static com.personal.groucho.game.constants.Character.grouchoPower;
import static com.personal.groucho.game.constants.Character.grouchoSpeed;
import static com.personal.groucho.game.constants.Character.skeletonHealth;
import static com.personal.groucho.game.constants.Character.skeletonPower;
import static com.personal.groucho.game.constants.Character.skeletonSpeed;
import static com.personal.groucho.game.constants.Character.wolfHealth;
import static com.personal.groucho.game.constants.Character.wolfPower;
import static com.personal.groucho.game.constants.Character.wolfSpeed;
import static com.personal.groucho.game.constants.Character.zombieHealth;
import static com.personal.groucho.game.constants.Character.zombiePower;
import static com.personal.groucho.game.constants.Character.zombieSpeed;

import com.personal.groucho.badlogic.androidgames.framework.Sound;
import com.personal.groucho.game.assets.Sounds;

public class CharacterFactory {
    public static class CharacterProp {
        public int health = 0;
        public int power = 0;
        public float speed = 0;

        public Spritesheet sheetIdle = null;
        public Spritesheet sheetWalk = null;
        public Spritesheet sheetAim = null;
        public Spritesheet sheetFire = null;
        public Spritesheet sheetDoor = null;
        public Spritesheet sheetHurt = null;
        public Spritesheet sheetDeath = null;
        public Sound call = null;
    }

    public static CharacterProp getGroucho(){
        CharacterProp groucho = new CharacterProp();

        groucho.health = grouchoHealth;
        groucho.power = grouchoPower;
        groucho.speed = grouchoSpeed;
        groucho.sheetIdle = grouchoIdle;
        groucho.sheetWalk = grouchoWalk;
        groucho.sheetAim = grouchoAim;
        groucho.sheetFire = grouchoFire;
        groucho.sheetDeath = grouchoDeath;

        return groucho;
    }

    public static CharacterProp getZombie() {
        CharacterProp zombie = new CharacterProp();

        zombie.health = zombieHealth;
        zombie.speed = zombieSpeed;
        zombie.power = zombiePower;
        zombie.sheetIdle = zombieIdle;
        zombie.sheetWalk = zombieWalk;
        zombie.sheetHurt = zombieHurt;
        zombie.sheetDeath = zombieDeath;
        zombie.call = Sounds.zombie;

        return zombie;
    }

    public static CharacterProp getSkeleton() {
        CharacterProp skeleton = new CharacterProp();

        skeleton.health = skeletonHealth;
        skeleton.speed = skeletonSpeed;
        skeleton.power = skeletonPower;
        skeleton.sheetIdle = skeletonIdle;
        skeleton.sheetWalk = skeletonWalk;
        skeleton.sheetHurt = skeletonHurt;
        skeleton.sheetDeath = skeletonDeath;
        skeleton.call = Sounds.skeleton;

        return skeleton;
    }

    public static CharacterProp getWolf() {
        CharacterProp wolf = new CharacterProp();

        wolf.health = wolfHealth;
        wolf.speed = wolfSpeed;
        wolf.power = wolfPower;
        wolf.sheetIdle = wolfIdle;
        wolf.sheetWalk = wolfWalk;
        wolf.sheetHurt = wolfHurt;
        wolf.sheetDeath = wolfDeath;
        wolf.call = Sounds.wolf;

        return wolf;
    }
}
