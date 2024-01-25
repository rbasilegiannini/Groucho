package com.personal.groucho.game.controller.states;

import android.graphics.Color;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.Controller;

public class Aiming extends ControllerState{

    private static Aiming state = null;

    private Aiming(Controller controller) {
        super(controller);
        name = NameState.AIMING;
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
            controller.setAimColor(Color.RED);
        }
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleTriggerTouchUp() {
        controller.setCurrentState(Idle.getInstance(controller));
    }
}
