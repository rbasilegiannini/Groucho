package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Idle extends ControllerState{

    public Idle(Controller controller) {
        super(controller);
    }

    @Override
    public void handleDPadTouchDown(int pointer, Orientation orientation) {
        controller.setDpadPointer(pointer);
        controller.setOrientation(orientation);
        controller.setCurrentState(new Walking(controller));
    }

    @Override
    public void handleTriggerTouchDragged(float x, float y) {
        controller.setCurrentState(new Aiming(controller));
    }

    @Override
    public void handleDPadTouchDragged(Orientation orientation) {
        controller.setOrientation(orientation);
        controller.setCurrentState(new Walking(controller));
    }

    @Override
    public void handleDPadTouchUp() {

    }

    @Override
    public void handleTriggerTouchUp() {

    }
}
