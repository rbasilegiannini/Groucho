package com.personal.groucho.game.states;

import android.graphics.Color;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Aiming extends ControllerState{

    private static Aiming state = null;

    private Aiming(Controller controller) {
        super(controller);
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null)
            state = new Aiming(controller);
        return state;
    }

    @Override
    public void handleDPadTouchDown(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleTriggerTouchDragged(float x, float y) {
        if (controller.isOnLoadingArea(x, y)) {
            controller.setCurrentState(Loading.getInstance(controller));
            controller.arcPaint.setColor(Color.RED);
        }
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleDPadTouchUp() {}

    @Override
    public void handleTriggerTouchUp() {
        controller.setCurrentState(Idle.getInstance(controller));
    }
}
