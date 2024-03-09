package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.UP;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;

public class HallwayEvents {
    public static void firstTimeInRoomEvent(Hallway room) {
        GameWorld gw = room.gameWorld;
        room.setControllerVisibility(false);

        room.grouchoTalk(gw.activity.getString(R.string.groucho_hallway_init1),
                room.playerPosX, room.playerPosY);
        room.level.eventChain.addAction(()-> {
            gw.controller.bulb.setVisibility(true);
            room.grouchoTalk(gw.activity.getString(R.string.groucho_hallway_init2),
                    room.playerPosX, room.playerPosY);
        });
        room.level.eventChain.addAction(()-> {
            gw.controller.handleLightTouchDown();
            gw.controller.dpad.setVisibility(true);
            gw.controller.pause.setVisibility(true);
        });

        Hallway.firstTime = false;
    }

    public static void fromGrouchoRoomEvent(Hallway room) {
        room.playerPosX = (int) (2.5*room.unit);
        room.playerPosY = (int) (1.7*room.unit);
        room.playerOrientation = UP;
    }

    public static void fromLibraryEvent(Hallway room) {
        room.playerPosX = (int) (10.5*room.unit);
        room.playerPosY = room.unit;
        room.playerOrientation = DOWN;
    }

    public static void grouchoRoomDoorEvent(Hallway room) {
        door.play(1f);
        room.level.goToGrouchoRoom();
    }

    public static void libraryDoorEvent(Hallway room) {
        door.play(1f);
        room.level.fromHallwayToLibrary = true;
        room.level.fromEntryHallToLibrary = false;
        room.level.goToLibrary();
    }

    public static void talkEvent(Hallway room, int sentenceId){
        GameWorld gw = room.gameWorld;
        String sentence = gw.activity.getString(sentenceId);
        room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
    }
}