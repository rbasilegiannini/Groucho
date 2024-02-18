package com.personal.groucho.game.AI.states;

import android.util.Log;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Attack extends AIState {
    private static Attack state = null;

    private Attack(AIComponent aiComponent) {
        super(aiComponent);
        name = StateName.ATTACK;
    }

    public static AIState getInstance(AIComponent aiComponent) {
        if (state == null) {
            state = new Attack(aiComponent);
        }
        return state;
    }

    @Override
    public List<Action> entryActions() {
        Log.i("State", "I'm entering in Attack state....");
        actions.clear();
        actions.add(() -> owner.entryAttackAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.activeAttackAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        actions.clear();
        actions.add(() -> owner.exitAttackAction());

        return actions;
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(EngageTransition.getInstance(owner));

        return transitions;
    }
}
