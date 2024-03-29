package com.personal.groucho.game.controller.states;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.Controller;

public class Walking extends ControllerState{
    private static Walking state = null;

    private Walking() {
        super();
        name = StateName.WALKING;
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null) {
            state = new Walking();
        }
        state.controller = controller;
        return state;
    }

    @Override
    public void handleDPadTouchDown(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
    }

    @Override
    public void handleDPadTouchUp() {
        controller.setCurrentState(Idle.getInstance(controller));
    }
}
