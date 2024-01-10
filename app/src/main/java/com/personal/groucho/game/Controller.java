package com.personal.groucho.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Controller {

    private float upDPadPosX, upDPadPosY;
    private float downDPadPosX, downDPadPosY;
    private float leftDPadPosX, leftDPadPosY;
    private float rightDPadPosX, rightDPadPosY;

    private int outerCircleRadius = 50;
    private Paint innerCirclePaint;
    private Paint outerCirclePaint;

    public Controller(float controllerCenterX, float controllerCenterY) {
        upDPadPosX = controllerCenterX;
        upDPadPosY = controllerCenterY + 75;

        downDPadPosX = controllerCenterX;
        downDPadPosY = controllerCenterY - 75;

        leftDPadPosX = controllerCenterX - 75;
        leftDPadPosY = controllerCenterY;

        rightDPadPosX = controllerCenterX + 75;
        rightDPadPosY = controllerCenterY;

        // paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        // Draw up D-Pad
        canvas.drawCircle(
                upDPadPosX,
                upDPadPosY,
                outerCircleRadius,
                outerCirclePaint
        );

        // Draw down D-Pad
        canvas.drawCircle(
                downDPadPosX,
                downDPadPosY,
                outerCircleRadius,
                outerCirclePaint
        );

        // Draw left D-Pad
        canvas.drawCircle(
                leftDPadPosX,
                leftDPadPosY,
                outerCircleRadius,
                outerCirclePaint
        );

        // Draw right D-Pad
        canvas.drawCircle(
                rightDPadPosX,
                rightDPadPosY,
                outerCircleRadius,
                outerCirclePaint
        );
    }

    public void update() {

    }
}
