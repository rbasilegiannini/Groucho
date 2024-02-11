package com.personal.groucho.game.levels;

import android.graphics.Canvas;

import com.personal.groucho.game.gameobjects.GameObject;

public abstract class Level {
    public abstract void init();
    public abstract void draw(Canvas canvas);
    public abstract void handleTrigger(GameObject trigger);
}
