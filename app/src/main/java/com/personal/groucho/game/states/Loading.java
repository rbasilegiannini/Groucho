package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Loading extends ControllerState{

    private static Loading state = null;

    private Loading(Controller controller) {
        super(controller);
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null)
            state = new Loading(controller);
        return state;
    }

    @Override
    public void handleDPadTouchDown(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleTriggerTouchDragged(float x, float y) {
        if (controller.isOnShootingArea(x, y))
            controller.setCurrentState(Shooting.getInstance(controller));
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
