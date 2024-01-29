package com.personal.groucho.game.AI.states;

import android.util.Log;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.EngageTransition;
import com.personal.groucho.game.AI.transitions.PatrolTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class Idle extends State {

    public Idle(AIComponent aiComponent) {
        super(aiComponent);
    }

    @Override
    public List<Action> entryActions() {
        Log.i("State", "I'm entering in Idle state....");
        actions.clear();
        actions.add(new Action() {
            @Override
            public void doIt() {
                owner.entryIdleAction();
            }
        });

        return actions;
    }

    @Override
    public List<Action> activeActions() {
        return Collections.emptyList();
    }

    @Override
    public List<Action> exitActions() {
        Log.i("State", "I'm leaving Idle state....");
        return Collections.emptyList();
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(new PatrolTransition(owner));
        transitions.add(new EngageTransition(owner));

        return transitions;
    }
}