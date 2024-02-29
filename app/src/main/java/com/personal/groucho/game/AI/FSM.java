package com.personal.groucho.game.AI;

import java.util.ArrayList;
import java.util.List;

public class FSM {
    private AIState currentState;
    private final List<Action> currentActions = new ArrayList<>();

    public FSM(){}

    public void setCurrentState(AIState currentState){
        this.currentState = currentState;
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

    public void setState(AIState newState) {currentState = newState;}
}
