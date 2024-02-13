package com.personal.groucho.game.controller;

import static com.personal.groucho.game.Utils.isInCircle;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DPad extends Widget{
    private float upPosX, upPosY, downPosX, downPosY;
    private float leftPosX, leftPosY, rightPosX, rightPosY;
    private final float radius;
    private final float radiusSqr;
    private final Paint circlePaint;

    public DPad(float cx, float cy) {
        super(cx, cy);

        float offset = 75;
        radius = 75;
        radiusSqr = (float) Math.pow(radius, 2);

        upPosX = centerX;
        upPosY = centerY - offset;
        downPosX = centerX;
        downPosY = centerY + offset;
        leftPosX = centerX - offset;
        leftPosY = centerY;
        rightPosX = centerX + offset;
        rightPosY = centerY;

        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void updateWidgetPosition(float increaseX, float increaseY) {
        upPosX += increaseX;
        upPosY += increaseY;
        downPosX += increaseX;
        downPosY += increaseY;

        leftPosX += increaseX;
        leftPosY += increaseY;
        rightPosX += increaseX;
        rightPosY += increaseY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(upPosX, upPosY, (int) radius, circlePaint);
        canvas.drawCircle(downPosX, downPosY, (int) radius, circlePaint);
        canvas.drawCircle(leftPosX, leftPosY, (int) radius, circlePaint);
        canvas.drawCircle(rightPosX, rightPosY, (int) radius, circlePaint);
    }

    public boolean isOnRight(float x, float y) {
        return isInCircle(rightPosX, rightPosY, x, y, radiusSqr);
    }
    public boolean isOnLeft(float x, float y) {
        return isInCircle(leftPosX, leftPosY, x, y, radiusSqr);
    }
    public boolean isOnDown(float x, float y) {
        return isInCircle(downPosX, downPosY, x, y, radiusSqr);
    }
    public boolean isOnUp(float x, float y) {
        return isInCircle(upPosX, upPosY, x, y, radiusSqr);
    }
}
