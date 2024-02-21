package com.personal.groucho.game.AI.states;

import android.util.Log;

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
        Log.i("State", "I'm entering in Investigate state....");
        actions.clear();
        actions.add(() -> owner.entryInvestigateAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.activeInvestigateAction());

        return actions;    }

    @Override
    public List<Action> exitActions() {
        Log.i("State", "I'm leaving Investigate state....");
        actions.clear();
        actions.add(() -> owner.exitInvestigateAction());

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
