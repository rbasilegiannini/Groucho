package com.personal.groucho.game;

public interface ControllerSubject {
    void addControllerListener(ControllerObserver observer);
    void removeControllerListener(ControllerObserver observer);
    void notifyToListeners();
}
