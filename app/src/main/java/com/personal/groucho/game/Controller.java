package com.personal.groucho.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.personal.groucho.badlogic.androidgames.framework.Input;

public class Controller {
    private final GameWorld gameWorld;
    private PlayerState playerState;
    private Orientation orientation;

    private final float upDPadPosX, upDPadPosY, downDPadPosX, downDPadPosY;
    private final float leftDPadPosX, leftDPadPosY, rightDPadPosX, rightDPadPosY;
    private final float loadPosX, loadPosY, shootPosX, shootPosY;
    private final float originalTriggerPosX, originalTriggerPosY;
    private float triggerPosX, triggerPosY;

    private boolean load;

    private final double dpadRadius, dpadRadiusSqr, triggerRadius;

    private final Paint circlePaint;
    private Paint arcPaint;

    private int DpadPointer, triggerPointer;

    public Controller(float controllerCenterX, float controllerCenterY, GameWorld gw) {
        gameWorld = gw;
        playerState = PlayerState.IDLE;
        orientation = Orientation.DOWN;

        dpadRadius = 75;
        dpadRadiusSqr = Math.pow(dpadRadius, 2);
        triggerRadius = 50;

        upDPadPosX = controllerCenterX;
        upDPadPosY = controllerCenterY - 75;

        downDPadPosX = controllerCenterX;
        downDPadPosY = controllerCenterY + 75;

        leftDPadPosX = controllerCenterX - 75;
        leftDPadPosY = controllerCenterY;

        rightDPadPosX = controllerCenterX + 75;
        rightDPadPosY = controllerCenterY;

        loadPosX = gameWorld.buffer.getWidth() - 300;
        loadPosY = controllerCenterY - 200;
        originalTriggerPosX = loadPosX +2*(int)triggerRadius;
        originalTriggerPosY = loadPosY +2*(int)triggerRadius+150;
        shootPosX = loadPosX;
        shootPosY = originalTriggerPosY+ (int)triggerRadius;
        triggerPosX = originalTriggerPosX;
        triggerPosY = originalTriggerPosY;

        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        arcPaint = new Paint();
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcPaint.setAlpha(150);

        triggerPointer = -1;
        DpadPointer = -1;
    }

    public void draw(Canvas canvas) {
        drawMovementControls(canvas);
        drawShootingControls(canvas);
    }

    private void drawMovementControls(Canvas canvas) {
        canvas.drawCircle(upDPadPosX, upDPadPosY, (int)dpadRadius, circlePaint);
        canvas.drawCircle(downDPadPosX, downDPadPosY, (int)dpadRadius, circlePaint);
        canvas.drawCircle(leftDPadPosX, leftDPadPosY, (int)dpadRadius, circlePaint);
        canvas.drawCircle(rightDPadPosX, rightDPadPosY, (int)dpadRadius, circlePaint);
    }

    private void drawShootingControls(Canvas canvas) {
        canvas.drawArc(loadPosX, loadPosY, loadPosX + 200, loadPosY + 200,
                225, 90, false, arcPaint);
        canvas.drawArc(shootPosX, shootPosY, shootPosX + 200, shootPosY + 200,
                45, 90, false, arcPaint);
        canvas.drawCircle(triggerPosX, triggerPosY, (int)triggerRadius, circlePaint);
    }

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

    private void consumeTouchDown(Input.TouchEvent event) {
        float x = gameWorld.fromScreenToBufferX(event.x);
        float y = gameWorld.fromScreenToBufferY(event.y);

        if (isOnUp(x, y))
            handleDPadTouchDown(event.pointer, Orientation.UP);
        else if (isOnDown(x, y))
            handleDPadTouchDown(event.pointer, Orientation.DOWN);
        else if (isOnLeft(x, y))
            handleDPadTouchDown(event.pointer, Orientation.LEFT);
        else if (isOnRight(x, y))
            handleDPadTouchDown(event.pointer, Orientation.RIGHT);
        else if(isOnTrigger(x,y)) {
            triggerPointer = event.pointer;
            playerState = PlayerState.AIMING;
        }
    }

    private void handleDPadTouchDown(int pointer, Orientation orientation) {
        DpadPointer = pointer;
        if (playerState == PlayerState.IDLE) {
            playerState = PlayerState.WALKING;
            this.orientation = orientation;
        }
        else if (playerState == PlayerState.AIMING || playerState == PlayerState.WALKING)
            this.orientation = orientation;
    }

    private void consumeTouchUp(Input.TouchEvent event) {
        if (event.pointer == DpadPointer) {
            playerState = PlayerState.IDLE;
            DpadPointer = -1;
        }
        else if (event.pointer == triggerPointer) {
            playerState = PlayerState.IDLE;
            triggerPointer = -1;
            resetTrigger();
        }
    }

    private void resetTrigger() {
        triggerPosX = originalTriggerPosX;
        triggerPosY = originalTriggerPosY;
        if(load) load = false;
        arcPaint.setColor(Color.GRAY);
    }

    private void consumeTouchDragged(Input.TouchEvent event) {
        float x = gameWorld.fromScreenToBufferX(event.x);
        float y = gameWorld.fromScreenToBufferY(event.y);

        if (event.pointer == DpadPointer) {
            if (isOnUp(x, y))
                handleDPadTouchDragged(Orientation.UP);
            else if (isOnDown(x, y))
                handleDPadTouchDragged(Orientation.DOWN);
            else if (isOnLeft(x, y))
                handleDPadTouchDragged(Orientation.LEFT);
            else if (isOnRight(x, y))
                handleDPadTouchDragged(Orientation.RIGHT);
        }
        else if (event.pointer == triggerPointer)
            handleTriggerTouchDragged(x, y);
    }

    private void handleTriggerTouchDragged(float x, float y) {
        triggerPosX = x;
        triggerPosY = y;

        if (playerState == PlayerState.AIMING) {
            if (isOnLoadingArea(x, y)) {
                load = true;
                arcPaint.setColor(Color.RED);
            } else if (isOnShootingArea(x, y)) {
                if (load) playerState = PlayerState.SHOOTING;
                arcPaint.setColor(Color.GREEN);
            }
        }
        else if (playerState == PlayerState.IDLE)
            playerState = PlayerState.AIMING;
    }

    private void handleDPadTouchDragged(Orientation orientation) {
        if (playerState == PlayerState.IDLE) {
            playerState = PlayerState.WALKING;
            this.orientation = orientation;
        }
        else if (playerState == PlayerState.AIMING || playerState == PlayerState.WALKING)
            this.orientation = orientation;
    }

    private boolean isOnRight(float x, float y) { return isInCircle(x, rightDPadPosX, y, rightDPadPosY); }
    private boolean isOnLeft(float x, float y) { return isInCircle(x, leftDPadPosX, y, leftDPadPosY); }
    private boolean isOnDown(float x, float y) { return isInCircle(x, downDPadPosX, y, downDPadPosY); }
    private boolean isOnUp(float x, float y) { return isInCircle(x, upDPadPosX, y, upDPadPosY); }
    private boolean isOnTrigger(float x, float y) { return isInCircle(x, triggerPosX, y, triggerPosY);}
    private boolean isOnLoadingArea(float x, float y) {
        return isInCircle(x, loadPosX +100, y, (float) (loadPosY +triggerRadius/2));
    }
    private boolean isOnShootingArea(float x, float y) {
        return isInCircle(x, shootPosX +100, y, (float) (shootPosY+3*triggerRadius));
    }

    private boolean isInCircle(float x, float posX, float y, float posY) {
        double pointerPositionSqr = Math.pow(x - posX, 2) + Math.pow(y - posY, 2);
        return pointerPositionSqr <= dpadRadiusSqr;
    }

    public void consumeShoot() {
        playerState = PlayerState.AIMING;
        load = false;
    }

    public PlayerState getPlayerState() {return playerState;}
    public Orientation getOrientation() {return orientation;}

}