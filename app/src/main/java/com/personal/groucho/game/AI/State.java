package com.personal.groucho.game.AI;

import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class State {
    protected AIComponent owner;
    protected final List<Action> actions = new ArrayList<>();
    protected final List<Transition> transitions = new ArrayList<>();

    protected State(AIComponent aiComponent) {owner = aiComponent;}

    public abstract List<Action> entryActions();
    public abstract List<Action> activeActions();
    public abstract List<Action> exitActions();
    public abstract List<Transition> outgoingTransitions();
}
