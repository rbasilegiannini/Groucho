package com.personal.groucho.game;

import java.util.ArrayList;

public class EventChain {
    private final ArrayList<Runnable> actions = new ArrayList<>();

    public void addAction(Runnable runnable) {actions.add(runnable);}
    public void consumeEvent() {
        Runnable action = actions.remove(0);
        action.run();
    }

    public boolean isEmpty() {return actions.isEmpty();}
}
