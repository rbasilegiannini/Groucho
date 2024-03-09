package com.personal.groucho.game.levels.first;

import static com.personal.groucho.game.assets.Sounds.door;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.UP;

import com.personal.groucho.R;
import com.personal.groucho.game.GameWorld;

public class LibraryEvents {

    public static void firstTimeInRoomEvent(Library room) {
        GameWorld gw = room.gameWorld;
        fromHallwayEvent(room);

        room.setControllerVisibility(false);
        room.grouchoTalk(gw.activity.getString(R.string.groucho_library_init1),
                room.playerPosX, room.playerPosY);
        room.level.eventChain.addAction(()-> {
            gw.player.setOrientation(DOWN);
            room.grouchoTalk(gw.activity.getString(R.string.groucho_library_init2),
                    room.playerPosX, room.playerPosY);
        });
        room.level.eventChain.addAction(()->room.setControllerVisibility(true));

        Library.firstTime = false;
    }

    public static void fromHallwayEvent(Library room){
        room.playerPosX = (int) (4.5 * room.unit);
        room.playerPosY = (int) (7.5 * room.unit);
        room.playerOrientation = UP;
    }

    public static void fromEntryWallEvent(Library room) {
        room.playerPosX = (int) (4.5*room.unit);
        room.playerPosY = cellSize;
        room.playerOrientation = DOWN;
    }

    public static void hallwayDoorEvent(Library room){
        door.play(1f);
        room.level.fromLibraryToHallway = true;
        room.level.fromGrouchoRoomToHallway = false;
        room.level.goToHallway();
    }

    public static void entryHallDoorEvent(Library room){
        GameWorld gw = room.gameWorld;
        if (room.isZombieDead) {
            door.play(1f);
            room.level.fromLibraryToEntryHall = true;
            room.level.fromBathroomToEntryHall = false;
            room.level.fromKitchenToEntryHall = false;
            room.level.fromGardenToEntryHall = false;
            room.level.goToEntryHall();
        }
        else {
            room.grouchoTalk(gw.activity.getString(R.string.groucho_library_closed_doors),
                    gw.player.posX, gw.player.posY);
        }
    }
}
