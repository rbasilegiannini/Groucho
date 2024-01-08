package com.personal.groucho.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Spritesheet {

    private Bitmap sheet;

    private int frameWidth;
    private int frameHeight;

    private int[] start;
    private int[] length;
    private int[] delay;

    public Spritesheet(Bitmap sheet, int numberOfAnimations) {
        this.sheet = sheet;
        this.start = new int[numberOfAnimations];
        this.length = new int[numberOfAnimations];
        this.delay = new int[numberOfAnimations];
    }

    public void setFrameSize(int width, int height) {
        frameWidth = width;
        frameHeight = height;
    }

    public void setAnimation(int animation, int firstFrame, int numberOfFrame, int delay) {
        this.start[animation] = firstFrame;
        this.length[animation] = numberOfFrame;
        this.delay[animation] = delay;
    }

    public void drawFrame(Canvas canvas, int frame, int x, int y) {
        //
    }

    public void drawAnimation(Canvas canvas, int animation, int step, int x, int y) {
        //
    }
}
