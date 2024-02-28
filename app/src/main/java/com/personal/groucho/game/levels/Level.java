package com.personal.groucho.game.levels;

import android.graphics.Canvas;

public abstract class Level {
    protected Room activeRoom;

    public abstract void init();

    public void draw(Canvas canvas) {
        activeRoom.draw(canvas);
    }
}
