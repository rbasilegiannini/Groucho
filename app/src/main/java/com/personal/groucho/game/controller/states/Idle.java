package com.personal.groucho.game.controller.states;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.Controller;

public class Idle extends ControllerState{

    private static Idle state = null;

    private Idle() {
        super();
        name = StateName.IDLE;
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null) {
            state = new Idle();
        }
        state.controller = controller;
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
