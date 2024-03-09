package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.controller.Orientation.UP;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;

public class GrouchoRoomEvents {

    public static void firstTimeInRoomEvent(GrouchoRoom room){
        GameWorld gw = room.gameWorld;

        room.playerPosX = 500;
        room.playerPosY = 500;
        room.setControllerVisibility(false);
        room.grouchoTalk(gw.activity.getString(R.string.groucho_bedroom_init1), room.playerPosX, room.playerPosY);
        room.level.eventChain.addAction(()-> {
            gw.player.setOrientation(UP);
            room.dylanTalk(gw.activity.getString(R.string.dylan_bedroom_init), 600, 500);
        });
        room.level.eventChain.addAction(()->
                room.grouchoTalk(gw.activity.getString(R.string.groucho_bedroom_init2),
                        room.playerPosX, room.playerPosY));
        room.level.eventChain.addAction(()->{
            gw.controller.dpad.setVisibility(true);
            gw.controller.pause.setVisibility(true);
        });

        GrouchoRoom.firstTime = false;
    }

    public static void talkEvent(GrouchoRoom room, int sentenceId){
        GameWorld gw = room.gameWorld;
        String sentence = gw.activity.getString(sentenceId);
        room.grouchoTalk(sentence, gw.player.posX, gw.player.posY);
    }

    public static void hallwayDoorEvent(GrouchoRoom room) {
        door.play(1f);
        room.level.fromLibraryToHallway = false;
        room.level.fromGrouchoRoomToHallway = true;
        room.level.goToHallway();
    }
}
