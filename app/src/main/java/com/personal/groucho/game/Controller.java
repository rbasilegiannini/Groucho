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
    private final float shootPosX, shootPosY;
    private final float triggerPosX;
    private final float triggerPosY;

    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean triggerPressed;
    private boolean load;

    private final double radius, radiusSqr;

    private final Paint circlePaint;
    private Paint arcPaint;

    public Controller(float controllerCenterX, float controllerCenterY, GameWorld gw) {
        gameWorld = gw;

        radius = 50;
        radiusSqr = Math.pow(radius, 2);

        upDPadPosX = controllerCenterX;
        upDPadPosY = controllerCenterY - 75;

        downDPadPosX = controllerCenterX;
        downDPadPosY = controllerCenterY + 75;

        leftDPadPosX = controllerCenterX - 75;
        leftDPadPosY = controllerCenterY;

        rightDPadPosX = controllerCenterX + 75;
        rightDPadPosY = controllerCenterY;

        shootPosX = gameWorld.buffer.getWidth() - 300;
        shootPosY = controllerCenterY - 200;

        triggerPosX = shootPosX+2*(int)radius;
        triggerPosY = shootPosY+2*(int)radius+150;

        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        arcPaint = new Paint();
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        drawMovementControls(canvas);
        drawShootingControls(canvas);
    }

    private void drawMovementControls(Canvas canvas) {
        // Draw up D-Pad
        canvas.drawCircle(upDPadPosX, upDPadPosY, (int)radius, circlePaint);

        // Draw down D-Pad
        canvas.drawCircle(downDPadPosX, downDPadPosY, (int)radius, circlePaint);

        // Draw left D-Pad
        canvas.drawCircle(leftDPadPosX, leftDPadPosY, (int)radius, circlePaint);

        // Draw right D-Pad
        canvas.drawCircle(rightDPadPosX, rightDPadPosY, (int)radius, circlePaint);
    }

    private void drawShootingControls(Canvas canvas) {
        canvas.drawArc(shootPosX, shootPosY,shootPosX + 200,shootPosY + 200,
                225, 90, false, arcPaint);

        canvas.drawCircle(triggerPosX, triggerPosY, (int)radius, circlePaint);
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

        if (upPressed && !isOnUp(x, y))
            upPressed = false;

        if (downPressed && !isOnDown(x, y))
            downPressed = false;

        if (leftPressed && !isOnLeft(x, y))
            leftPressed = false;

        if (rightPressed && !isOnRight(x, y))
            rightPressed = false;

        if (triggerPressed && isOnShootArea(x, y)) {
            load = true;
            // Sound loading
            // Change color of arc paint
            arcPaint.setColor(Color.RED);
        }

        if (triggerPressed && !isOnShootArea(x, y)) {
            load = false;
            // Restore color of arc paint
            arcPaint.setColor(Color.GRAY);
        }
    }

    private void consumeTouchUp(Input.TouchEvent event) {
        float x = gameWorld.fromScreenToBufferX(event.x);
        float y = gameWorld.fromScreenToBufferY(event.y);

        if(isOnUp(x, y))
            if(upPressed) upPressed = false;

        if (isOnDown(x, y))
            if(downPressed) downPressed = false;

        if (isOnLeft(x, y))
            if(leftPressed) leftPressed = false;

        if (isOnRight(x, y))
            if(rightPressed) rightPressed = false;

        if (triggerPressed) {
            if(load) shoot();
            triggerPressed = false;
            Log.i("Controller", "trigger up");
        }

    }

    private void consumeTouchDown(Input.TouchEvent event) {
        float x = gameWorld.fromScreenToBufferX(event.x);
        float y = gameWorld.fromScreenToBufferY(event.y);

        if(isOnUp(x, y))
            if(!upPressed) upPressed = true;

        if (isOnDown(x, y))
            if(!downPressed) downPressed = true;

        if (isOnLeft(x,y))
            if(!leftPressed) leftPressed = true;

        if (isOnRight(x,y))
            if(!rightPressed) rightPressed = true;

        if(isOnTrigger(x,y))
            if(!triggerPressed) {
                triggerPressed = true;
                Log.i("Controller", "trigger down");
            }
    }

    private boolean isOnRight(float x, float y) { return isInCircle(x, rightDPadPosX, y, rightDPadPosY); }
    private boolean isOnLeft(float x, float y) { return isInCircle(x, leftDPadPosX, y, leftDPadPosY); }
    private boolean isOnDown(float x, float y) { return isInCircle(x, downDPadPosX, y, downDPadPosY); }
    private boolean isOnUp(float x, float y) { return isInCircle(x, upDPadPosX, y, upDPadPosY); }
    private boolean isOnTrigger(float x, float y) { return isInCircle(x, triggerPosX, y, triggerPosY);}
    private boolean isOnShootArea(float x, float y) {
        return isInCircle(x, shootPosX+100, y, shootPosY+100);
    }

    private boolean isInCircle(float x, float upDPadPosX, float y, float upDPadPosY) {
        double pointerPositionSqr = Math.pow(x - upDPadPosX, 2) + Math.pow(y - upDPadPosY, 2);
        return pointerPositionSqr <= radiusSqr;
    }

    private void shoot() {
        // Fire!
        // Sound of shot
        arcPaint.setColor(Color.GRAY);
        Log.i("Controller", "Fire!");
    }

    public boolean isIdle(){
        return !(upPressed || downPressed || rightPressed || leftPressed || triggerPressed);
    }

}