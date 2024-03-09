package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Textures.littleDresser;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.TextureComponent;

public class BathroomEvents {

    public static void firstTimeInRoomEvent(Bathroom room) {
        GameWorld gw = room.gameWorld;
        String sentence = gw.activity.getString(R.string.groucho_bathroom_init);
        room.grouchoTalk(sentence, room.playerPosX, room.playerPosY);
        Bathroom.firstTime = false;
    }

    public static void dresserWithKeyEvent(Bathroom room){
        GameWorld gw = room.gameWorld;
        if (!room.level.bathroomKey) {
            TextureComponent textureComp = (TextureComponent)  room.bathroomKey.getComponent(ComponentType.DRAWABLE);
            textureComp.init(littleDresser, 100, 100);
            room.level.bathroomKey = true;
            room.level.counterKeys++;
            String sentence = gw.activity.getString(R.string.groucho_keys)
                    + " " +  room.level.counterKeys + ".";
            room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
        }
    }

    public static void entryHallDoorEvent(Bathroom room) {
        room.level.fromBathroomToEntryHall = true;
        room.level.fromGardenToEntryHall = false;
        room.level.fromLibraryToEntryHall = false;
        room.level.fromKitchenToEntryHall = false;
        door.play(1f);
        room.level.goToEntryHall();
    }

    public static void talkEvent(Bathroom room, int sentenceId){
        GameWorld gw = room.gameWorld;
        String sentence = gw.activity.getString(sentenceId);
        room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
    }
}
