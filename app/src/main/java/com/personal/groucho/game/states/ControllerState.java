package com.personal.groucho.game.states;

import com.personal.groucho.game.Controller;
import com.personal.groucho.game.Orientation;

public abstract class ControllerState {

    protected Controller controller;

    public ControllerState(Controller controller) {this.controller = controller;}

    public abstract void handleDPadTouchDown(int pointer, Orientation orientation);
    public abstract void handleTriggerTouchDragged(float x, float y);
    public abstract void handleDPadTouchDragged(Orientation orientation);
    public abstract void handleDPadTouchUp();
    public abstract void handleTriggerTouchUp();
}
