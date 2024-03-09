package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;

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
}
