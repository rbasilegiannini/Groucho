package com.personal.groucho.game.levels.first;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.levels.Level;
import com.personal.groucho.game.levels.Room;

public class FirstLevel extends Level {
    private final Room grouchoRoom;
    private final Room hallway;

    public FirstLevel(GameWorld gameWorld){
        grouchoRoom = new GrouchoRoom(gameWorld, this);
        hallway = new Hallway(gameWorld, this);
    }

    public void init(){
        activeRoom = grouchoRoom;
        activeRoom.init();
    }

    public void goToHallway() {
        activeRoom.releaseRoom();
        activeRoom = hallway;
        activeRoom.init();
    }

    public void goToGrouchoRoom() {
        activeRoom.releaseRoom();
        activeRoom = grouchoRoom;
        activeRoom.init();
    }
}
