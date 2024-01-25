package com.personal.groucho.game.controller;

public interface ControllerSubject {
    void addControllerListener(ControllerObserver observer);
    void removeControllerListener(ControllerObserver observer);
    void notifyToListeners();
}
