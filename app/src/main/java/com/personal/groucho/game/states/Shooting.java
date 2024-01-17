package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public class Shooting extends ControllerState{

    public Shooting(Controller controller) {
        super(controller);
    }

    @Override
    public void handleDPadTouchDown(int pointer, Orientation orientation) {
        controller.setDpadPointer(pointer);
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

    }

    @Override
    public void handleTriggerTouchUp() {

    }
}
