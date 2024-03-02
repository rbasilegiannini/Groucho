package com.personal.groucho.game;

import static com.personal.groucho.game.assets.Spritesheets.grouchoAim;
import static com.personal.groucho.game.assets.Spritesheets.grouchoDeath;
import static com.personal.groucho.game.assets.Spritesheets.grouchoDoor;
import static com.personal.groucho.game.assets.Spritesheets.grouchoFire;
import static com.personal.groucho.game.assets.Spritesheets.grouchoIdle;
import static com.personal.groucho.game.assets.Spritesheets.grouchoWalk;
import static com.personal.groucho.game.assets.Spritesheets.skeletonDeath;
import static com.personal.groucho.game.assets.Spritesheets.skeletonHurt;
import static com.personal.groucho.game.assets.Spritesheets.skeletonIdle;
import static com.personal.groucho.game.assets.Spritesheets.skeletonWalk;
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
import static com.personal.groucho.game.constants.Character.zombieHealth;
import static com.personal.groucho.game.constants.Character.zombiePower;
import static com.personal.groucho.game.constants.Character.zombieSpeed;

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
        groucho.sheetDoor = grouchoDoor;
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

        return skeleton;
    }
}
