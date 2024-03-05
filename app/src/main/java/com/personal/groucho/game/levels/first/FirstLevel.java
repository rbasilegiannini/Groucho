package com.personal.groucho.game.levels.first;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.levels.Level;
import com.personal.groucho.game.levels.Room;

public class FirstLevel extends Level {
    private final Room grouchoRoom;
    private final Room hallway;
    private final Room library;
    private final Room entryHall;
    protected boolean fromEntryHallToLibrary,fromHallwayToLibrary;
    protected boolean fromGrouchoRoomToHallway, fromLibraryToHallway, fromLibraryToEntryHall;
    protected boolean key1 = false, key2 = false, key3 = false;

    public FirstLevel(GameWorld gameWorld){
        grouchoRoom = new GrouchoRoom(gameWorld, this);
        hallway = new Hallway(gameWorld, this);
        library = new Library(gameWorld, this);
        entryHall = new EntryHall(gameWorld, this);
    }

    public void init(){
        activeRoom = entryHall;
        activeRoom.init();
    }

    public void goToLibrary(){
        changeRoom(library);
    }

    public void goToHallway() {
        changeRoom(hallway);
    }

    public void goToGrouchoRoom() {
        changeRoom(grouchoRoom);
    }

    public void goToEntryHall() {
        changeRoom(entryHall);
    }

    private void changeRoom(Room hall) {
        activeRoom.releaseRoom();
        activeRoom = hall;
        activeRoom.init();
    }

    protected boolean isEndGame() {return key1 && key2 && key3;}
}
