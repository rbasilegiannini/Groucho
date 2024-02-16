package com.personal.groucho.game.AI.states;

import android.util.Log;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.AttackTransition;
import com.personal.groucho.game.AI.transitions.IdleTransition;
import com.personal.groucho.game.AI.transitions.PatrolTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Engage extends AIState {
    private static Engage state = null;

    private Engage(AIComponent aiComponent) {
        super(aiComponent);
    }

    public static AIState getInstance(AIComponent aiComponent) {
        if (state == null) {
            state = new Engage(aiComponent);
        }
        return state;
    }

    @Override
    public List<Action> entryActions() {
        Log.i("State", "I'm entering in Engage state....");
        actions.clear();
        actions.add(() -> owner.entryEngageAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.activeEngageAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        Log.i("State", "I'm leaving Engage state....");
        actions.clear();
        actions.add(() -> owner.exitEngageAction());
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
