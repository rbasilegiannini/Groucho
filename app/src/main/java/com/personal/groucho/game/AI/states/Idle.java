package com.personal.groucho.game.AI.states;

import static com.personal.groucho.game.controller.states.StateName.IDLE;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.AI.transitions.InvestigateTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Idle extends AIState {

    public Idle(AIComponent aiComponent) {
        super(IDLE, aiComponent);
    }

    @Override
    public List<Action> entryActions() {
        actions.clear();
        actions.add(() -> owner.idleActions.entryAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.idleActions.activeAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        actions.clear();
        actions.add(() -> owner.idleActions.exitAction());

        return actions;
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(EngageTransition.getInstance(owner));
        transitions.add(InvestigateTransition.getInstance(owner));

        return transitions;
    }
}