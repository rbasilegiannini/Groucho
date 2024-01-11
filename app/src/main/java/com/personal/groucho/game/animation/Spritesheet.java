package com.personal.groucho.game.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Spritesheet {

    private final Bitmap sheet;

    private int frameWidth;
    private int frameHeight;

    private final int[] start;
    private final int[] length;
    private final int[] delay;

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

    public int drawAnimation(Canvas canvas, int animation, int step, int x, int y, int scaleFactor) {
//        boolean isLoop = true;
//        if (isLoop) {
//            if (step >= length[animation] - 1)
//                step = start[animation] + 1;
//        }
//        else {
//            if (step >= length[animation] - 1)
//                step = length[animation] - 1;
//        }
        if (step >= length[animation] - 1)
            step = 0;

        int srcTop = animation * frameHeight;
        int srcLeft = step * frameWidth;
        Rect src = new Rect(
                srcLeft, srcTop,
                srcLeft + frameWidth,
                srcTop + frameHeight
        );

        int destTop = y-(scaleFactor*frameHeight/2);
        int destLeft = x-(scaleFactor*frameWidth/2);
        Rect dest = new Rect(
                destLeft, destTop,
                destLeft + scaleFactor*frameWidth - 1,
                destTop + scaleFactor*frameHeight -1
        );

        canvas.drawBitmap(sheet, src, dest, null);
        return step;
    }

    public long getDelay(int animation) {
        return delay[animation];
    }
}
