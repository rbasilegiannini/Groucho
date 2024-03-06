package com.personal.groucho.game.AI.states;

import static com.personal.groucho.game.controller.states.StateName.ATTACK;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Attack extends AIState {

    public Attack(AIComponent aiComponent) {
        super(ATTACK, aiComponent);
    }

    @Override
    public List<Action> entryActions() {
        actions.clear();
        actions.add(() -> owner.attackActions.entryAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.attackActions.activeAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        actions.clear();
        actions.add(() -> owner.attackActions.exitAction());

        return actions;
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(EngageTransition.getInstance(owner));

        return transitions;
    }
}
