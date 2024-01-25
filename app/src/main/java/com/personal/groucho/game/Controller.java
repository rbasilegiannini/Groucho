package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.fromScreenToBufferX;
import static com.personal.groucho.game.Utils.fromScreenToBufferY;
import static com.personal.groucho.game.assets.Textures.lightbulb;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.game.states.Aiming;
import com.personal.groucho.game.states.ControllerState;

import java.util.ArrayList;
import java.util.List;

public class Controller implements ControllerSubject{
    private Orientation orientation;

    private float upDPadPosX, upDPadPosY, downDPadPosX, downDPadPosY;
    private float leftDPadPosX, leftDPadPosY, rightDPadPosX, rightDPadPosY;
    private float loadPosX, loadPosY, shootPosX, shootPosY;
    private float originalTriggerPosX, originalTriggerPosY;
    private float triggerPosX, triggerPosY;
    private float lightPosX;
    private float lightPosY;
    private final Rect src;
    private final Rect dest;
    private final double dpadRadius, dpadRadiusSqr, triggerRadius;

    private final Paint circlePaint;
    private final Paint arcPaint;
    private final Paint lightPaint;

    private int dpadPointer, triggerPointer;

    private float offsetX, offsetY;

    private ControllerState currentState;
    private final List<ControllerObserver> controllerObservers = new ArrayList<>();

    private boolean turnOn;

    public Controller(float controllerCenterX, float controllerCenterY, GameWorld gw) {
        //currentState = Idle.getInstance(this);
        orientation = Orientation.DOWN;
        turnOn = false;

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

        loadPosX = gw.buffer.getWidth() - 300;
        loadPosY = controllerCenterY - 200;
        originalTriggerPosX = loadPosX +2*(int)triggerRadius;
        originalTriggerPosY = loadPosY +2*(int)triggerRadius+150;
        shootPosX = loadPosX;
        shootPosY = originalTriggerPosY+ (int)triggerRadius;
        triggerPosX = originalTriggerPosX;
        triggerPosY = originalTriggerPosY;
        lightPosX = (float) (loadPosX + 0.75*triggerRadius);
        lightPosY = loadPosY - 300;

        src = new Rect(0, 0, lightbulb.getWidth(), lightbulb.getHeight());
        dest = new Rect();

        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        arcPaint = new Paint();
        arcPaint.setColor(Color.GRAY);
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcPaint.setAlpha(150);

        lightPaint = new Paint();
        lightPaint.setAlpha(75);

        triggerPointer = -1;
        dpadPointer = -1;
    }

    public void draw(Canvas canvas) {
        drawMovementControls(canvas);
        drawShootingControls(canvas);
        drawFlashLight(canvas);
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

    private void drawFlashLight(Canvas canvas) {
        dest.top = (int) lightPosY;
        dest.left = (int) lightPosX;
        dest.right = (int) lightPosX + 128;
        dest.bottom = (int) lightPosY + 128;

        canvas.drawBitmap(lightbulb, src, dest, lightPaint);
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
        float x = fromScreenToBufferX(event.x) + offsetX;
        float y = fromScreenToBufferY(event.y) + offsetY;

        if (isOnLight(x, y))
            handleLightTouchDown();

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
            setCurrentState(Aiming.getInstance(this));
        }
    }

    private void handleLightTouchDown() {
        if (!turnOn) {
            turnOn = true;
            lightPaint.setAlpha(255);
        }
        else {
            turnOn = false;
            lightPaint.setAlpha(75);
        }
        for (ControllerObserver observer : controllerObservers)
            observer.switchLightEvent(turnOn);
    }

    private void handleDPadTouchDown(int pointer, Orientation orientation){
        dpadPointer = pointer;
        currentState.handleDPadTouchDown(orientation);
    }

    private void consumeTouchUp(Input.TouchEvent event) {
        if (event.pointer == dpadPointer) {
            currentState.handleDPadTouchUp();
            dpadPointer = -1;
        }
        else if (event.pointer == triggerPointer) {
            currentState.handleTriggerTouchUp();
            triggerPointer = -1;
            resetTrigger();
        }
    }

    private void resetTrigger() {
        triggerPosX = originalTriggerPosX;
        triggerPosY = originalTriggerPosY;
        resetAimColor();
    }

    private void resetAimColor() {
        arcPaint.setColor(Color.GRAY);
    }

    private void consumeTouchDragged(Input.TouchEvent event) {
        float x = fromScreenToBufferX(event.x) + offsetX;
        float y = fromScreenToBufferY(event.y) + offsetY;

        if (event.pointer == dpadPointer) {
            if (isOnUp(x, y))
                currentState.handleDPadTouchDragged(Orientation.UP);
            else if (isOnDown(x, y))
                currentState.handleDPadTouchDragged(Orientation.DOWN);
            else if (isOnLeft(x, y))
                currentState.handleDPadTouchDragged(Orientation.LEFT);
            else if (isOnRight(x, y))
                currentState.handleDPadTouchDragged(Orientation.RIGHT);
        }
        else if (event.pointer == triggerPointer){
            triggerPosX = x;
            triggerPosY = y;
            currentState.handleTriggerTouchDragged(x, y);
        }
    }

    private boolean isOnLight(float x, float y) {return isInCircle(x, lightPosX+64, y, lightPosY+64);}
    private boolean isOnRight(float x, float y) { return isInCircle(x, rightDPadPosX, y, rightDPadPosY); }
    private boolean isOnLeft(float x, float y) { return isInCircle(x, leftDPadPosX, y, leftDPadPosY); }
    private boolean isOnDown(float x, float y) { return isInCircle(x, downDPadPosX, y, downDPadPosY); }
    private boolean isOnUp(float x, float y) { return isInCircle(x, upDPadPosX, y, upDPadPosY); }
    private boolean isOnTrigger(float x, float y) { return isInCircle(x, triggerPosX, y, triggerPosY);}
    public boolean isOnLoadingArea(float x, float y) {
        return isInCircle(x, loadPosX +100, y, (float) (loadPosY +triggerRadius/2));
    }
    public boolean isOnShootingArea(float x, float y) {
        return isInCircle(x, shootPosX +100, y, (float) (shootPosY+3*triggerRadius));
    }

    private boolean isInCircle(float x, float posX, float y, float posY) {
        double pointerPositionSqr = Math.pow(x - posX, 2) + Math.pow(y - posY, 2);
        return pointerPositionSqr <= dpadRadiusSqr;
    }

    public void consumeShoot() {
        setCurrentState(Aiming.getInstance(this));
        resetAimColor();
    }

    public ControllerState getPlayerState() {return currentState;}
    public Orientation getOrientation() {return orientation;}

    @Override
    public void addControllerListener(ControllerObserver observer) {
        controllerObservers.add(observer);
    }

    @Override
    public void removeControllerListener(ControllerObserver observer) {
        controllerObservers.remove(observer);
    }

    @Override
    public void notifyToListeners() {
        for (ControllerObserver observer : controllerObservers)
            observer.update(currentState);
    }

    public void setCurrentState(ControllerState state) {
        currentState = state;
        notifyToListeners();
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        notifyToListeners();
    }

    public void setAimColor(int color) {arcPaint.setColor(color);}

    public void updateControllerPosition(float increaseX, float increaseY) {
        offsetX += increaseX;
        offsetY += increaseY;

        upDPadPosX += increaseX;
        upDPadPosY += increaseY;
        downDPadPosX += increaseX;
        downDPadPosY += increaseY;

        leftDPadPosX += increaseX;
        leftDPadPosY += increaseY;
        rightDPadPosX += increaseX;
        rightDPadPosY += increaseY;

        loadPosX += increaseX;
        loadPosY += increaseY;
        shootPosX += increaseX;
        shootPosY += increaseY;

        originalTriggerPosX += increaseX;
        originalTriggerPosY += increaseY;
        triggerPosX += increaseX;
        triggerPosY += increaseY;

        lightPosX += increaseX;
        lightPosY += increaseY;
    }
}