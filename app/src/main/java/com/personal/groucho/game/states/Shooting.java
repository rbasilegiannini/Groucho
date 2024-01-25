package com.personal.groucho.game.states;

import com.personal.groucho.game.Orientation;
import com.personal.groucho.game.controller.Controller;

public class Shooting extends ControllerState{

    private static Shooting state = null;

    private Shooting(Controller controller) {
        super(controller);
        name = NameState.SHOOTING;
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null)
            state = new Shooting(controller);
        return state;
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
    }
}
