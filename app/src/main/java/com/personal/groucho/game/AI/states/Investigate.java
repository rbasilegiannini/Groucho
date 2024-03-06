package com.personal.groucho.game.AI.states;

import static com.personal.groucho.game.controller.states.StateName.INVESTIGATE;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.AI.transitions.IdleTransition;
import com.personal.groucho.game.AI.transitions.PatrolTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Investigate extends AIState {

    public Investigate(AIComponent aiComponent) {
        super(INVESTIGATE, aiComponent);
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
