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
import static com.personal.groucho.game.constants.Character.grouchoHealth;
import static com.personal.groucho.game.constants.Character.grouchoPower;
import static com.personal.groucho.game.constants.Character.grouchoSpeed;
import static com.personal.groucho.game.constants.Character.skeletonHealth;
import static com.personal.groucho.game.constants.Character.skeletonPower;
import static com.personal.groucho.game.constants.Character.skeletonSpeed;

public class CharacterFactory {
    public static class CharacterProperties {
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

    public static CharacterProperties getSkeleton() {
        CharacterProperties skeleton = new CharacterProperties();

        skeleton.health = skeletonHealth;
        skeleton.speed = skeletonSpeed;
        skeleton.power = skeletonPower;
        skeleton.sheetIdle = skeletonIdle;
        skeleton.sheetWalk = skeletonWalk;
        skeleton.sheetHurt = skeletonHurt;
        skeleton.sheetDeath = skeletonDeath;

        return skeleton;
    }

    public static CharacterProperties getGroucho(){
        CharacterProperties groucho = new CharacterProperties();

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

}
