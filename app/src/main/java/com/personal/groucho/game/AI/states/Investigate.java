package com.personal.groucho.game.AI.states;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.AI.transitions.IdleTransition;
import com.personal.groucho.game.AI.transitions.PatrolTransition;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Investigate extends AIState {
    private static Investigate state = null;

    private Investigate() {
        super();
        name = StateName.INVESTIGATE;
    }

    public static AIState getInstance(AIComponent aiComponent) {
        if (state == null) {
            state = new Investigate();
        }
        state.owner = aiComponent;
        return state;
    }

    @Override
    public List<Action> entryActions() {
        actions.clear();
        actions.add(() -> owner.investigateActions.entryAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.investigateActions.activeAction());

        return actions;    }

    @Override
    public List<Action> exitActions() {
        actions.clear();
        actions.add(() -> owner.investigateActions.exitAction());

        return actions;
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(EngageTransition.getInstance(owner));
        transitions.add(IdleTransition.getInstance(owner));
        transitions.add(PatrolTransition.getInstance(owner));

        return transitions;
    }
}
