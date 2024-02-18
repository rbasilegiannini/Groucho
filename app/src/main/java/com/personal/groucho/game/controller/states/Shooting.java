package com.personal.groucho.game.controller.states;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.Controller;

public class Shooting extends ControllerState{

    private static Shooting state = null;

    private Shooting(Controller controller) {
        super(controller);
        name = StateName.SHOOTING;
    }

    public static ControllerState getInstance(Controller controller) {
        if (state == null) {
            state = new Shooting(controller);
        }
        return state;
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
    }
}
