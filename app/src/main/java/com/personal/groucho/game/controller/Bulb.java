package com.personal.groucho.game.controller;

import static com.personal.groucho.game.Utils.isInCircle;
import static com.personal.groucho.game.assets.Textures.lightBulb;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Bulb extends Widget{
    private float lightPosX, lightPosY;
    private final float distanceSqr;
    private final Rect src, dest;
    private final int minAlpha, maxAlpha;
    private final Paint lightPaint;
    private boolean turnOn;

    public Bulb(float cx, float cy){
        super(cx, cy);
        float distance = 50;
        distanceSqr = (float) Math.pow(distance, 2);
        turnOn = false;

        lightPosX = (float) (centerX - 300 + 0.75*distance);
        lightPosY = centerY - 500;

        src = new Rect(0, 0, lightBulb.getWidth(), lightBulb.getHeight());
        dest = new Rect();

        minAlpha = 75;
        maxAlpha = 255;
        lightPaint = new Paint();
        lightPaint.setAlpha(minAlpha);
    }

    @Override
    public void updateWidgetPosition(float increaseX, float increaseY) {
        lightPosX += increaseX;
        lightPosY += increaseY;
    }

    @Override
    public void draw(Canvas canvas) {
        dest.top = (int) lightPosY;
        dest.left = (int) lightPosX;
        dest.right = (int) lightPosX + 128;
        dest.bottom = (int) lightPosY + 128;

        canvas.drawBitmap(lightBulb, src, dest, lightPaint);
    }

    public boolean switchLight(){
        if (!turnOn) {
            turnOn = true;
            lightPaint.setAlpha(maxAlpha);
        }
        else {
            turnOn = false;
            lightPaint.setAlpha(minAlpha);
        }

        return turnOn;
    }

    public boolean isOnLight(float x, float y) {
        return isInCircle(lightPosX+64, lightPosY+64, x, y, distanceSqr);
    }
}
