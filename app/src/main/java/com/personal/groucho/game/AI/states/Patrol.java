package com.personal.groucho.game.AI.states;

import android.util.Log;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.AI.transitions.InvestigateTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Patrol extends State {

    public Patrol(AIComponent aiComponent) {
        super(aiComponent);
    }

    @Override
    public List<Action> entryActions() {
        Log.i("State", "I'm entering in Patrol state....");
        actions.clear();
        actions.add(() -> owner.entryPatrolAction());

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        actions.clear();
        actions.add(() -> owner.activePatrolAction());

        return actions;
    }

    @Override
    public List<Action> exitActions() {
        Log.i("State", "I'm leaving Patrol state....");
        actions.clear();
        actions.add(() -> owner.exitPatrolAction());

        return actions;
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(new EngageTransition(owner));
        transitions.add(new InvestigateTransition(owner));

        return transitions;
    }
}
