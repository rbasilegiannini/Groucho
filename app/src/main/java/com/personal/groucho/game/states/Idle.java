package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Idle extends ControllerState{

    private static Idle state = null;

    private Idle(Controller controller) {
        super(controller);
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null)
            state = new Idle(controller);
        return state;
    }

    @Override
    public void handleDPadTouchDown(Orientation orientation) {
        controller.setOrientation(orientation);
        controller.setCurrentState(Walking.getInstance(controller));
    }

    @Override
    public void handleTriggerTouchDragged(float x, float y) {
        controller.setCurrentState(Aiming.getInstance(controller));
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
        controller.setCurrentState(Walking.getInstance(controller));
    }
}
