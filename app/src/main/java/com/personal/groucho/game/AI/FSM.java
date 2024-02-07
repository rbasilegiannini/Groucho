package com.personal.groucho.game.AI;

import com.personal.groucho.game.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class FSM {
    private State currentState;
    private final List<Action> currentActions;

    public FSM (State initialState) {
        currentState = initialState;
        currentActions = new ArrayList<>();
    }

    public List<Action> getActions() {
        currentActions.clear();

        for (Transition transition : currentState.outgoingTransitions()) {
            if(transition.guard().eval()){
                currentActions.addAll(transition.actions());
                currentActions.addAll(currentState.exitActions());
                currentState = transition.targetState();
                currentActions.addAll(currentState.entryActions());

                return currentActions;
            }
        }

        currentActions.addAll(currentState.activeActions());
        return currentActions;
    }
}
