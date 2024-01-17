package com.personal.groucho.game.states;

import android.graphics.Color;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Aiming extends ControllerState{

    public Aiming(Controller controller) {
        super(controller);
    }

    @Override
    public void handleDPadTouchDown(int pointer, Orientation orientation) {
        controller.setDpadPointer(pointer);
        controller.setOrientation(orientation);
    }

    @Override
    public void handleTriggerTouchDragged(float x, float y) {
        if (controller.isOnLoadingArea(x, y)) {
            controller.setCurrentState(new Loading(controller));
            controller.arcPaint.setColor(Color.RED);
        }
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleDPadTouchUp() {
    }

    @Override
    public void handleTriggerTouchUp() {
        controller.setCurrentState(new Idle(controller));
    }
}
