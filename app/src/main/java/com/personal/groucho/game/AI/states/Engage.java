package com.personal.groucho.game.AI.states;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.AttackTransition;
import com.personal.groucho.game.AI.transitions.IdleTransition;
import com.personal.groucho.game.AI.transitions.PatrolTransition;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Engage extends AIState {
    private static Engage state = null;

    private Engage() {
        super();
        name = StateName.ENGAGE;
    }

    public static AIState getInstance(AIComponent aiComponent) {
        if (state == null) {
            state = new Engage();
        }
        state.owner = aiComponent;
        return state;
    }

    @Override
    public List<Action> entryActions() {
        actions.clear();
        actions.add(() -> owner.engageActions.entryAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.engageActions.activeAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        actions.clear();
        actions.add(() -> owner.engageActions.exitAction());

        return actions;
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(IdleTransition.getInstance(owner));
        transitions.add(PatrolTransition.getInstance(owner));
        transitions.add(AttackTransition.getInstance(owner));

        return transitions;
    }
}
