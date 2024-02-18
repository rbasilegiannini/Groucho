package com.personal.groucho.game.controller.states;

import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.Controller;

public abstract class ControllerState {

    protected Controller controller;
    protected StateName name;

    protected ControllerState(Controller controller) {this.controller = controller;}

    public void handleDPadTouchDown(Orientation orientation){}
    public void handleTriggerTouchDragged(float x, float y){}
    public void handleDPadTouchDragged(Orientation orientation){}
    public void handleDPadTouchUp(){}
    public void handleTriggerTouchUp(){}

    public StateName getName() {return name;}
}
