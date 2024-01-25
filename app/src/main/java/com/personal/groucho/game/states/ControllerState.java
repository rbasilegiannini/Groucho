package com.personal.groucho.game.states;

import com.personal.groucho.game.Orientation;
import com.personal.groucho.game.controller.Controller;

public abstract class ControllerState {

    protected Controller controller;
    protected NameState name;

    protected ControllerState(Controller controller) {this.controller = controller;}

    public void handleDPadTouchDown(Orientation orientation){}
    public void handleTriggerTouchDragged(float x, float y){}
    public void handleDPadTouchDragged(Orientation orientation){}
    public void handleDPadTouchUp(){}
    public void handleTriggerTouchUp(){}

    public NameState getName() {return name;}
}
