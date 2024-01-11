package com.personal.groucho.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.personal.groucho.badlogic.androidgames.framework.Input;

public class Controller {
    private final GameWorld gameWorld;

    private final float upDPadPosX, upDPadPosY;
    private final float downDPadPosX, downDPadPosY;
    private final float leftDPadPosX, leftDPadPosY;
    private final float rightDPadPosX, rightDPadPosY;

    boolean upPressed, downPressed, leftPressed, rightPressed;

    private final double radius, radiusSqr;

    private final Paint circlePaint;

    public Controller(float controllerCenterX, float controllerCenterY, GameWorld gw) {
        gameWorld = gw;

        upDPadPosX = controllerCenterX;
        upDPadPosY = controllerCenterY - 75;

        downDPadPosX = controllerCenterX;
        downDPadPosY = controllerCenterY + 75;

        leftDPadPosX = controllerCenterX - 75;
        leftDPadPosY = controllerCenterY;

        rightDPadPosX = controllerCenterX + 75;
        rightDPadPosY = controllerCenterY;

        radius = 50;
        radiusSqr = Math.pow(radius, 2);

        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        // Draw up D-Pad
        canvas.drawCircle(
                upDPadPosX,
                upDPadPosY,
                (int)radius,
                circlePaint
        );

        // Draw down D-Pad
        canvas.drawCircle(
                downDPadPosX,
                downDPadPosY,
                (int)radius,
                circlePaint
        );

        // Draw left D-Pad
        canvas.drawCircle(
                leftDPadPosX,
                leftDPadPosY,
                (int)radius,
                circlePaint
        );

        // Draw right D-Pad
        canvas.drawCircle(
                rightDPadPosX,
                rightDPadPosY,
                (int)radius,
                circlePaint
        );
    }

    public boolean isUpPressed() {return upPressed;}
    public boolean isDownPressed() {return downPressed;}
    public boolean isLeftPressed() {return leftPressed;}
    public boolean isRightPressed() {return rightPressed;}

    public void consumeTouchEvent(Input.TouchEvent event){
        switch (event.type) {
            case Input.TouchEvent.TOUCH_DOWN:
                consumeTouchDown(event);
                break;
            case Input.TouchEvent.TOUCH_UP:
                consumeTouchUp(event);
                break;
            case Input.TouchEvent.TOUCH_DRAGGED:
                consumeTouchDragged(event);
                break;
        }
    }

    private void consumeTouchDragged(Input.TouchEvent event) {
        float x = gameWorld.fromScreenToBufferX(event.x);
        float y = gameWorld.fromScreenToBufferY(event.y);

        if (upPressed && !isInUp(x, y)) {upPressed = false;

            Log.d("Controller", "released Up");}
        if (downPressed && !isInDown(x, y)){
            if(downPressed) {downPressed = false;

                Log.d("Controller", "released Down");}
        }
        if (leftPressed && !isInLeft(x,y)) {
            if(leftPressed) {leftPressed = false;

                Log.d("Controller", "released Left");}
        }
        if (rightPressed && !isInRight(x,y)) {
            if(rightPressed) {rightPressed = false;

                Log.d("Controller", "released Right");}
        }
    }

    private void consumeTouchUp(Input.TouchEvent event) {
        float x = gameWorld.fromScreenToBufferX(event.x);
        float y = gameWorld.fromScreenToBufferY(event.y);

        if(isInUp(x, y)){
            if(upPressed) {upPressed = false;

                Log.d("Controller", "released Up");}
        }
        if (isInDown(x, y)){
            if(downPressed) {downPressed = false;

                Log.d("Controller", "released Down");}
        }
        if (isInLeft(x,y)) {
            if(leftPressed) {leftPressed = false;

                Log.d("Controller", "released Left");}
        }
        if (isInRight(x,y)) {
            if(rightPressed) {rightPressed = false;

                Log.d("Controller", "released Right");}
        }
    }

    private void consumeTouchDown(Input.TouchEvent event) {
        float x = gameWorld.fromScreenToBufferX(event.x);
        float y = gameWorld.fromScreenToBufferY(event.y);

        if(isInUp(x, y)){
            if(!upPressed) {upPressed = true;

            Log.d("Controller", "pressed Up");}
        }
        if (isInDown(x, y)){
            if(!downPressed) {downPressed = true;

            Log.d("Controller", "pressed Down");}
        }
        if (isInLeft(x,y)) {
            if(!leftPressed) {leftPressed = true;

            Log.d("Controller", "pressed Left");}
        }
        if (isInRight(x,y)) {
            if(!rightPressed) {rightPressed = true;

            Log.d("Controller", "pressed Right");}
        }
    }

    private boolean isInRight(float x, float y) { return isInCircle(x, rightDPadPosX, y, rightDPadPosY); }
    private boolean isInLeft(float x, float y) { return isInCircle(x, leftDPadPosX, y, leftDPadPosY); }
    private boolean isInDown(float x, float y) { return isInCircle(x, downDPadPosX, y, downDPadPosY); }
    private boolean isInUp(float x, float y) { return isInCircle(x, upDPadPosX, y, upDPadPosY); }

    private boolean isInCircle(float x, float upDPadPosX, float y, float upDPadPosY) {
        double pointerPositionSqr = Math.pow(x - upDPadPosX, 2) + Math.pow(y - upDPadPosY, 2);
        return pointerPositionSqr <= radiusSqr;
    }

}