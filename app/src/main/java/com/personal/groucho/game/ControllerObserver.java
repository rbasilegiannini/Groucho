package com.personal.groucho.game;

import com.personal.groucho.game.states.ControllerState;

public interface ControllerObserver {
    void update(ControllerState currentState);
    void switchLightEvent(boolean isTurnOn);
}
