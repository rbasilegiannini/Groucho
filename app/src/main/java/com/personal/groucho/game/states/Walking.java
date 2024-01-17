package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Walking extends ControllerState{

    public Walking(Controller controller) {
        super(controller);
    }

    @Override
    public void handleDPadTouchDown(int pointer, Orientation orientation) {
        controller.setDpadPointer(pointer);
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
        controller.setCurrentState(new Idle(controller));
    }

    @Override
    public void handleTriggerTouchUp() {

    }
}
