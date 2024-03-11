package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Sounds.key;
import static com.personal.groucho.game.assets.Spritesheets.wolfDeathWithoutKey;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.components.SpriteComponent;

public class KitchenEvents {

    public static void firstTimeInRoomEvent(Kitchen room){
        GameWorld gw = room.gameWorld;
        String sentence = gw.activity.getString(R.string.groucho_kitchen_init);
        room.grouchoTalk(sentence, room.playerPosX, room.playerPosY);
        room.firstTime = false;
    }

    public static void entryHallDoorEvent(Kitchen room) {
        room.level.fromBathroomToEntryHall = false;
        room.level.fromGardenToEntryHall = false;
        room.level.fromLibraryToEntryHall = false;
        room.level.fromKitchenToEntryHall = true;
        door.play(1f);
        room.level.goToEntryHall();
    }

    public static void wolfDeath(Kitchen room){
        GameWorld gw = room.gameWorld;
        talkEvent(room, R.string.groucho_entryhall_afterwolf);

        if (!room.level.kitchenKey) {
            SpriteComponent spriteComp = (SpriteComponent) room.wolf.getComponent(DRAWABLE);
            spriteComp.setCurrentSpritesheet(wolfDeathWithoutKey);
            key.play(1f);
            room.level.kitchenKey = true;
            room.level.counterKeys++;
            String sentence = gw.activity.getString(R.string.groucho_keys)
                    + " " +  room.level.counterKeys + ".";
            room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
            gw.goHandler.removeGameObject(room.keyGO);
        }
    }

    public static void talkEvent(Kitchen room, int sentenceId){
        GameWorld gw = room.gameWorld;
        String sentence = gw.activity.getString(sentenceId);
        room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
    }
}
