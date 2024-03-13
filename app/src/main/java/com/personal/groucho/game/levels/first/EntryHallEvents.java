package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.assets.Sounds.throwing;
import static com.personal.groucho.game.assets.Textures.heavyDoorOpened;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.Orientation.UP;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.TextureComponent;

public class EntryHallEvents {
    public static void firstTimeInRoomEvent(EntryHall room){
        GameWorld gw = room.gameWorld;

        room.playerPosX = 3*room.unit;
        room.playerPosY = (int) (7.5*room.unit);
        String sentence = gw.activity.getString(R.string.dylan_bedroom_init);
        room.dylanTalk(sentence, room.playerPosX+2*room.unit, (int)room.playerPosY);
    }

    public static void fromLibraryEvent(EntryHall room){
        room.playerPosX = 3*room.unit;
        room.playerPosY = (int) (7.5*room.unit);
        room.playerOrientation = UP;
    }
    public static void fromBathroomEvent(EntryHall room){
        room.playerPosX = (int) (11*room.unit);
        room.playerPosY = (int) (2.0*room.unit);
        room.playerOrientation = LEFT;
    }
    public static void fromGardenEvent(EntryHall room){
        room.playerPosX = (int) (11*room.unit);
        room.playerPosY = (int) (6.0*room.unit);
        room.playerOrientation = LEFT;
    }
    public static void fromKitchenEvent(EntryHall room){
        room.playerPosX = room.unit;
        room.playerPosY = 3*room.unit;
        room.playerOrientation = RIGHT;
    }

    public static void firstTimeTryOpenHeavyDoorEvent(EntryHall room) {
        GameWorld gw = room.gameWorld;
        String dylanSentence = gw.activity.getString(R.string.dylan_entryhall_heavydoor);
        room.dylanTalk(dylanSentence, (int)(9.5 * room.unit), room.unit);

        room.level.eventChain.addAction(() ->{
            String grouchoSentence = gw.activity.getString(R.string.groucho_entryhall_heavydoor_init);
            room.grouchoTalk(grouchoSentence, gw.player.posX, gw.player.posY);
        });
        room.firstTime = false;
    }

    public static void tryOpenHeavyDoorEvent(EntryHall room){
        GameWorld gw = room.gameWorld;
        String dylanSentence = gw.activity.getString(R.string.dylan_entryhall_heavydoor);
        room.dylanTalk(dylanSentence, (int)(9.5 * room.unit), room.unit);

        room.level.eventChain.addAction(() -> {
            String grouchoSentence = gw.activity.getString(R.string.groucho_entryhall_heavydoor);
            room.grouchoTalk(grouchoSentence, gw.player.posX, gw.player.posY);
        });
    }

    public static void openHeavyDoorEvent(EntryHall room) {
        GameWorld gw = room.gameWorld;

        TextureComponent textureComp = (TextureComponent) room.heavyDoorGO.getComponent(ComponentType.DRAWABLE);
        textureComp.init(heavyDoorOpened, 320,280);

        door.play(1f);

        talkEvent(room, R.string.groucho_endgame_init1);

        room.level.eventChain.addAction(() -> throwing.play(1f));
        room.level.eventChain.addAction(() -> gw.player.setOrientation(DOWN));
        room.level.eventChain.addAction(() -> talkEvent(room, R.string.groucho_endgame_init2));
        room.level.eventChain.addAction(() -> gw.complete = true);
    }

    public static void libraryDoorEvent(EntryHall room) {
        GameWorld gw = room.gameWorld;
        String grouchoSentence = gw.activity.getString(R.string.groucho_library_closed);
        room.grouchoTalk(grouchoSentence, gw.player.posX, gw.player.posY);
    }

    public static void bathroomDoorEvent(EntryHall room) {
        GameWorld gw = room.gameWorld;

        if (!room.firstTime) {
            if (!room.level.bathroomKey) {
                door.play(1f);
                room.level.goToBathroom();
            } else {
                String sentence = gw.activity.getString(R.string.groucho_entryhall_room_complete);
                room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
            }
        }
        else {
            String sentence = gw.activity.getString(R.string.groucho_entrywall_closed_doors);
            room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
        }
    }

    public static void gardenDoorEvent(EntryHall room) {
        GameWorld gw = room.gameWorld;

        if (!room.firstTime) {
            if (!room.level.gardenKey) {
                door.play(1f);
                room.level.goToGarden();
            } else {
                String sentence = gw.activity.getString(R.string.groucho_entryhall_room_complete);
                room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
            }
        }
        else {
            String sentence = gw.activity.getString(R.string.groucho_entrywall_closed_doors);
            room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
        }
    }

    public static void kitchenDoorEvent(EntryHall room){
        GameWorld gw = room.gameWorld;

        if(!room.firstTime) {
            if (!room.level.kitchenKey) {
                door.play(1f);
                room.level.goToKitchen();
            } else {
                String sentence = gw.activity.getString(R.string.groucho_entryhall_room_complete);
                room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
            }
        }
        else {
            String sentence = gw.activity.getString(R.string.groucho_entrywall_closed_doors);
            room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
        }
    }

    public static void talkEvent(EntryHall room, int sentenceId){
        GameWorld gw = room.gameWorld;
        String sentence = gw.activity.getString(sentenceId);
        room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
    }
}
