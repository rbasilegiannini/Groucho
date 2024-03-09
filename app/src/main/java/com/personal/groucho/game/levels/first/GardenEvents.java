package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.controller.Orientation.DOWN;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.TextureComponent;

public class GardenEvents {
    public static void firstTimeInRoomEvent(Garden room) {
        GameWorld gw = room.gameWorld;

        room.playerOrientation = DOWN;
        String sentence = gw.activity.getString(R.string.groucho_garden_init);
        room.grouchoTalk(sentence, room.playerPosX, room.playerPosY);

        Garden.firstTime = false;
    }

    public static void entryHallDoorEvent(Garden room){
        room.level.fromBathroomToEntryHall = false;
        room.level.fromGardenToEntryHall = true;
        room.level.fromLibraryToEntryHall = false;
        room.level.fromKitchenToEntryHall = false;
        door.play(1f);
        room.level.goToEntryHall();
    }

    public static void statueWithKeyEvent(Garden room){
        GameWorld gw = room.gameWorld;
        if (room.skeletonsCounter == 0) {
            if (!room.level.gardenKey) {
                TextureComponent textureComp = (TextureComponent) room.statue.getComponent(ComponentType.DRAWABLE);
                textureComp.init(Textures.statue, (int) (1.7 * room.unit), 3 * room.unit);
                room.level.gardenKey = true;
                room.level.counterKeys++;
                String sentence = gw.activity.getString(R.string.groucho_keys)
                        + " " + room.level.counterKeys + ".";
                room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
            }
        }
        else {
            String sentence = gw.activity.getString(R.string.groucho_garden_statue);
            room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
        }
    }
}
