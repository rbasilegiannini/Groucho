package com.personal.groucho.game.controller;

import static com.personal.groucho.game.Utils.isInCircle;
import static com.personal.groucho.game.assets.Textures.pause;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Pause extends Widget{
    private float posX, posY;
    private final float distanceSqr;
    private final Rect src, dest;
    private final Paint paint;

    protected Pause(float centerX, float centerY) {
        super(centerX, centerY);

        float distance = 100;
        distanceSqr = (float) Math.pow(distance, 2);

        posX = centerX - 70;
        posY = centerY + 350;

        src = new Rect(0, 0, pause.getWidth(), pause.getHeight());
        dest = new Rect();

        paint = new Paint();
        paint.setAlpha(150);
    }

    @Override
    public void updateWidgetPosition(float increaseX, float increaseY) {
        posX += increaseX;
        posY += increaseY;
    }

    @Override
    public void draw(Canvas canvas) {
        if (visible) {
            dest.top = (int) posY;
            dest.left = (int) posX;
            dest.right = (int) posX + 128;
            dest.bottom = (int) posY + 128;

            canvas.drawBitmap(pause, src, dest, paint);
        }
    }

    public boolean isOnPause(float x, float y) {
        if(!visible) return false;
        return isInCircle(posX +64, posY +64, x, y, distanceSqr);
    }
}
