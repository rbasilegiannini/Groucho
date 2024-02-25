package com.personal.groucho.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Spritesheet {

    private final Bitmap sheet;
    private int frameWidth;
    private int frameHeight;
    private final int[] start, length, delay;
    private final Rect src, dest;

    public Spritesheet(Bitmap sheet, int numberOfAnimations) {
        this.sheet = sheet;
        this.start = new int[numberOfAnimations];
        this.length = new int[numberOfAnimations];
        this.delay = new int[numberOfAnimations];
        this.src = new Rect();
        this.dest = new Rect();
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

    public int drawAnimation(Canvas canvas, int anim, int step, int x, int y, int scale, Paint paint) {
        if (step > length[anim] - 1)
            step = 0;

        src.left = step * frameWidth;
        src.top = anim * frameHeight;
        src.right = src.left + frameWidth;
        src.bottom = src.top + frameHeight;

        dest.left = x-(scale*frameWidth/2);
        dest.top = (int) (y-(0.85*scale*frameHeight));
        dest.right = dest.left + scale*frameWidth - 1;
        dest.bottom = dest.top + scale*frameHeight -1;

        canvas.drawBitmap(sheet, src, dest, paint);

        return step;
    }

    public long getDelay(int animation) {
        return delay[animation];
    }
    public int getLength(int animation) {return length[animation];}
}
