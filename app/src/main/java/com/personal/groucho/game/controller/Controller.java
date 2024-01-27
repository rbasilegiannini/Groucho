package com.personal.groucho.game.controller;

import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.Utils.fromScreenToBufferX;
import static com.personal.groucho.game.Utils.fromScreenToBufferY;

import android.graphics.Canvas;

import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.game.controller.states.Aiming;
import com.personal.groucho.game.controller.states.ControllerState;

import java.util.ArrayList;
import java.util.List;

public class Controller implements ControllerSubject {

    private final List<ControllerObserver> controllerObservers = new ArrayList<>();
    private ControllerState currentState;
    private Orientation orientation;

    private final DPad dpad;
    private final Trigger trigger;
    private final Bulb bulb;

    private float offsetX, offsetY;

    private int dpadPointer, triggerPointer;

    public Controller(float controllerCenterX, float controllerCenterY) {
        offsetX = 0;
        offsetY = 0;

        orientation = Orientation.DOWN;
        dpad = new DPad(controllerCenterX - (float) 0.40*bufferWidth, controllerCenterY);
        trigger = new Trigger(controllerCenterX + (float) bufferWidth /2, controllerCenterY);
        bulb = new Bulb(controllerCenterX + (float) bufferWidth/2, controllerCenterY);

        triggerPointer = -1;
        dpadPointer = -1;
    }

    // Observer
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
    public ControllerState getPlayerState() {return currentState;}
    public Orientation getOrientation() {return orientation;}

    // Handle widgets
    public void draw(Canvas canvas) {
        dpad.draw(canvas);
        trigger.draw(canvas);
        bulb.draw(canvas);
    }

    public void setAimColor(int color) {trigger.setAimColor(color);}
    public boolean isOnLoadingArea(float x, float y) {return trigger.isOnLoadingArea(x, y);}
    public boolean isOnShootingArea(float x, float y) {return trigger.isOnShootingArea(x, y);}

    public void updateControllerPosition(float increaseX, float increaseY) {
        offsetX += increaseX;
        offsetY += increaseY;

        dpad.updateWidgetPosition(increaseX, increaseY);
        trigger.updateWidgetPosition(increaseX, increaseY);
        bulb.updateWidgetPosition(increaseX, increaseY);
    }

    // Handle touch events
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

        if (bulb.isOnLight(x, y))
            handleLightTouchDown();

        if (dpad.isOnUp(x, y))
            handleDPadTouchDown(event.pointer, Orientation.UP);
        else if (dpad.isOnDown(x, y))
            handleDPadTouchDown(event.pointer, Orientation.DOWN);
        else if (dpad.isOnLeft(x, y))
            handleDPadTouchDown(event.pointer, Orientation.LEFT);
        else if (dpad.isOnRight(x, y))
            handleDPadTouchDown(event.pointer, Orientation.RIGHT);
        else if(trigger.isOnTrigger(x,y)) {
            triggerPointer = event.pointer;
            setCurrentState(Aiming.getInstance(this));
        }
    }

    private void handleLightTouchDown() {
        for (ControllerObserver observer : controllerObservers)
            observer.switchLightEvent(bulb.switchLight());
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
            trigger.resetTrigger();
        }
    }

    private void consumeTouchDragged(Input.TouchEvent event) {
        float x = fromScreenToBufferX(event.x) + offsetX;
        float y = fromScreenToBufferY(event.y) + offsetY;

        if (event.pointer == dpadPointer) {
            if (dpad.isOnUp(x, y))
                currentState.handleDPadTouchDragged(Orientation.UP);
            else if (dpad.isOnDown(x, y))
                currentState.handleDPadTouchDragged(Orientation.DOWN);
            else if (dpad.isOnLeft(x, y))
                currentState.handleDPadTouchDragged(Orientation.LEFT);
            else if (dpad.isOnRight(x, y))
                currentState.handleDPadTouchDragged(Orientation.RIGHT);
        }
        else if (event.pointer == triggerPointer){
            trigger.setPositionTrigger(x, y);
            currentState.handleTriggerTouchDragged(x, y);
        }
    }

    // Handle events
    public void consumeShoot() {
        setCurrentState(Aiming.getInstance(this));
        trigger.resetAimColor();
    }
}
