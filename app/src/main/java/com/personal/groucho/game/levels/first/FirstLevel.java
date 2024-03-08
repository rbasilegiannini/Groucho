package com.personal.groucho.game.levels.first;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.levels.Level;
import com.personal.groucho.game.levels.Room;

public class FirstLevel extends Level {
    private final Room grouchoRoom;
    private final Room hallway;
    private final Room library;
    private final Room entryHall;
    private final Room zombiesRoom;
    private final Room garden;
    private final Room wolfRoom;

    protected boolean fromZombieRoomToEntryHall, fromGardenToEntryHall, fromKitchenToEntryHall;
    protected int counterKeys = 0;
    protected boolean fromEntryHallToLibrary,fromHallwayToLibrary;
    protected boolean fromGrouchoRoomToHallway, fromLibraryToHallway, fromLibraryToEntryHall;
    protected boolean bathroomKey = false, gardenKey = false, kitchenKey = false;

    public FirstLevel(GameWorld gameWorld){
        grouchoRoom = new GrouchoRoom(gameWorld, this);
        hallway = new Hallway(gameWorld, this);
        library = new Library(gameWorld, this);
        entryHall = new EntryHall(gameWorld, this);
        zombiesRoom = new ZombiesRoom(gameWorld, this);
        garden = new Garden(gameWorld, this);
        wolfRoom = new Kitchen(gameWorld, this);
    }

    public void init(){
        activeRoom = wolfRoom;
        activeRoom.init();
    }

    public void goToLibrary(){changeRoom(library);}
    public void goToHallway() {changeRoom(hallway);}
    public void goToGrouchoRoom() {changeRoom(grouchoRoom);}
    public void goToEntryHall() {changeRoom(entryHall);}
    public void goToZombiesRoom() {changeRoom(zombiesRoom);}
    public void goToGarden() {changeRoom(garden);}
    public void goToKitchen() {changeRoom(wolfRoom);}

    private void changeRoom(Room hall) {
        activeRoom.releaseRoom();
        activeRoom = hall;
        activeRoom.init();
    }

    protected boolean isEndGame() {return bathroomKey && gardenKey && kitchenKey;}

}
