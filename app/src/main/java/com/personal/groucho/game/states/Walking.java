package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Walking extends ControllerState{

    private static Walking state = null;

    private Walking(Controller controller) {
        super(controller);
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null)
            state = new Walking(controller);
        return state;
    }

    @Override
    public void handleDPadTouchDown(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleTriggerTouchDragged(float x, float y) {

    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleDPadTouchUp() {
        controller.setCurrentState(Idle.getInstance(controller));
    }

    @Override
    public void handleTriggerTouchUp() {

    }
}
