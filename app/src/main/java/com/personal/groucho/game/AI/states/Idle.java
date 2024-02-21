package com.personal.groucho.game.AI.states;

import android.util.Log;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.AI.transitions.InvestigateTransition;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Idle extends AIState {
    private static Idle state = null;

    private Idle() {
        super();
        name = StateName.IDLE;
    }

    public static AIState getInstance(AIComponent aiComponent) {
        if (state == null) {
            state = new Idle();
        }
        state.owner = aiComponent;
        return state;
    }

    @Override
    public List<Action> entryActions() {
        Log.i("State", "I'm entering in Idle state....");
        actions.clear();
        actions.add(() -> owner.entryIdleAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {

        actions.clear();
        actions.add(() -> owner.activeIdleAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        Log.i("State", "I'm leaving Idle state....");
        actions.clear();
        actions.add(() -> owner.exitIdleAction());

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