package com.personal.groucho.game.controller;

import com.personal.groucho.game.controller.states.ControllerState;

public interface ControllerObserver {
    void update(ControllerState currentState);
    void switchLight(boolean isTurnOn);
    void pressPause();
}
