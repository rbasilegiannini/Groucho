package com.personal.groucho.game.AI;

import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.ArrayList;
import java.util.List;

public abstract class AIState {
    protected AIComponent owner;
    protected final List<Action> actions = new ArrayList<>();
    protected final List<Transition> transitions = new ArrayList<>();
    protected StateName name;

    protected AIState(StateName name, AIComponent aiComponent){
        this.name = name;
        this.owner = aiComponent;
    }

    public abstract List<Action> entryActions();
    public abstract List<Action> activeActions();
    public abstract List<Action> exitActions();
    public abstract List<Transition> outgoingTransitions();

    public StateName getName() {return name;}
}
