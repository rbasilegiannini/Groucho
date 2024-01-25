package com.personal.groucho.game.controller;

import android.graphics.Canvas;

public abstract class Widget {
    protected float centerX, centerY;

    protected Widget(float centerX, float centerY) {this.centerX = centerX; this.centerY=centerY;}
    public abstract void updateWidgetPosition(float increaseX, float increaseY);
    public abstract void draw(Canvas canvas);
}
