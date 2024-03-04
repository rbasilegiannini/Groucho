package com.personal.groucho.game.levels;

import android.graphics.Canvas;

import com.personal.groucho.game.EventChain;

public abstract class Level {
    protected Room activeRoom;
    public EventChain eventChain = new EventChain();

    public abstract void init();

    public void draw(Canvas canvas) {
        activeRoom.draw(canvas);
    }

    public Room getActiveRoom() {return activeRoom;}
}
