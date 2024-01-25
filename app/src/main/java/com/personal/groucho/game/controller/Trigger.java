package com.personal.groucho.game.controller;

import static com.personal.groucho.game.Utils.isInCircle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Trigger extends Widget{
    private float originalTriggerPosX, originalTriggerPosY;
    private float triggerPosX, triggerPosY;
    private float loadPosX, loadPosY, shootPosX, shootPosY;
    private final float radius, radiusSqr;

    private final Paint circlePaint;
    private final Paint arcPaint;

    public Trigger(float cx, float cy) {
        super(cx, cy);
        radius = 50;
        radiusSqr = (float) Math.pow(radius, 2);

        loadPosX = centerX - 300;
        loadPosY = centerY - 200;
        originalTriggerPosX = loadPosX +2*(int)radius;
        originalTriggerPosY = loadPosY +2*(int)radius+150;
        shootPosX = loadPosX;
        shootPosY = originalTriggerPosY+ (int)radius;
        triggerPosX = originalTriggerPosX;
        triggerPosY = originalTriggerPosY;

        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        arcPaint = new Paint();
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcPaint.setAlpha(150);
    }

    @Override
    public void updateWidgetPosition(float increaseX, float increaseY) {
        loadPosX += increaseX;
        loadPosY += increaseY;
        shootPosX += increaseX;
        shootPosY += increaseY;

        originalTriggerPosX += increaseX;
        originalTriggerPosY += increaseY;
        triggerPosX += increaseX;
        triggerPosY += increaseY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(loadPosX, loadPosY, loadPosX + 200, loadPosY + 200,
                225, 90, false, arcPaint);
        canvas.drawArc(shootPosX, shootPosY, shootPosX + 200, shootPosY + 200,
                45, 90, false, arcPaint);
        canvas.drawCircle(triggerPosX, triggerPosY, (int)radius, circlePaint);
    }

    public void setPositionTrigger(float x, float y) {
        triggerPosX = x;
        triggerPosY = y;
    }

    public void setAimColor(int color) {
        arcPaint.setColor(color);
    }

    public void resetTrigger() {
        triggerPosX = originalTriggerPosX;
        triggerPosY = originalTriggerPosY;
        resetAimColor();
    }

    public void resetAimColor() {
        arcPaint.setColor(Color.GRAY);
    }

    public boolean isOnTrigger(float x, float y) {
        return isInCircle(x, triggerPosX, y, triggerPosY, radiusSqr);
    }
    public boolean isOnLoadingArea(float x, float y) {
        return isInCircle(x, loadPosX +100, y, (float) (loadPosY +radius/2), radiusSqr);
    }
    public boolean isOnShootingArea(float x, float y) {
        return isInCircle(x, shootPosX +100, y, (float) (shootPosY+3*radius), radiusSqr);
    }
}
