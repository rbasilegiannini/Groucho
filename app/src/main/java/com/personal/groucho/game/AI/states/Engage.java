package com.personal.groucho.game.AI.states;

import static com.personal.groucho.game.controller.states.StateName.ENGAGE;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.AttackTransition;
import com.personal.groucho.game.AI.transitions.IdleTransition;
import com.personal.groucho.game.AI.transitions.PatrolTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Engage extends AIState {

    public Engage(AIComponent aiComponent) {
        super(ENGAGE, aiComponent);
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
