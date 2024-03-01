package com.personal.groucho.game.levels.first;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.levels.Level;
import com.personal.groucho.game.levels.Room;

public class FirstLevel extends Level {
    private final Room grouchoRoom;
    private final Room hallway;
    private final Room hall;
    protected boolean fromGrouchoRoom, fromHall;

    public FirstLevel(GameWorld gameWorld){
        grouchoRoom = new GrouchoRoom(gameWorld, this);
        hallway = new Hallway(gameWorld, this);
        hall = new Hall(gameWorld, this);
    }

    public void init(){
        activeRoom = grouchoRoom;
        activeRoom.init();
    }

    public void goToHall(){
        changeRoom(hall);
    }

    public void goToHallway() {
        changeRoom(hallway);
    }

    public void goToGrouchoRoom() {
        changeRoom(grouchoRoom);
    }

    private void changeRoom(Room hall) {
        activeRoom.releaseRoom();
        activeRoom = hall;
        activeRoom.init();
    }
}
