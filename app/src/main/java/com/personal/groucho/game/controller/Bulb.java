package com.personal.groucho.game.controller;

import static com.personal.groucho.game.Utils.isInCircle;
import static com.personal.groucho.game.assets.Textures.lightBulb;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bulb extends Widget{
    private float posX, posY;
    private final float distanceSqr;
    private final Rect src, dest;
    private final int minAlpha, maxAlpha;
    private final Paint paint;
    private boolean turnOn;

    public Bulb(float cx, float cy){
        super(cx, cy);
        float distance = 50;
        distanceSqr = (float) Math.pow(distance, 2);
        turnOn = false;

        posX = (float) (centerX - 300 + 0.75*distance);
        posY = centerY - 500;

        src = new Rect(0, 0, lightBulb.getWidth(), lightBulb.getHeight());
        dest = new Rect();

        minAlpha = 75;
        maxAlpha = 255;
        paint = new Paint();
        paint.setAlpha(minAlpha);
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

            canvas.drawBitmap(lightBulb, src, dest, paint);
        }
    }

    public boolean switchLight(){
        if (!turnOn) {
            turnOn = true;
            paint.setAlpha(maxAlpha);
        }
        else {
            turnOn = false;
            paint.setAlpha(minAlpha);
        }

        return turnOn;
    }

    public boolean isOnLight(float x, float y) {
        if(!visible) return false;
        return isInCircle(posX +64, posY +64, x, y, distanceSqr);
    }
}
