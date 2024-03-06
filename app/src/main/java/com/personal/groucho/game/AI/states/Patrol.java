package com.personal.groucho.game.AI.states;

import static com.personal.groucho.game.controller.states.StateName.PATROL;

import android.util.Log;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.AI.transitions.InvestigateTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Patrol extends AIState {

    public Patrol(AIComponent aiComponent) {
        super(PATROL, aiComponent);
    }

    @Override
    public List<Action> entryActions() {
        Log.i("State", "I'm entering in Patrol state....");
        actions.clear();
        actions.add(() -> owner.patrolActions.entryAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.patrolActions.activeAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        actions.clear();
        actions.add(() -> owner.patrolActions.exitAction());

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
