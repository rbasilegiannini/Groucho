package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Shooting extends ControllerState{

    private static Shooting state = null;

    private Shooting(Controller controller) {
        super(controller);
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
