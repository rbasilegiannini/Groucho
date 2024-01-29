package com.personal.groucho.game.AI.states;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.transitions.AttackTransition;
import com.personal.groucho.game.AI.transitions.PatrolTransition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class Engagement extends State {

    public Engagement(AIComponent aiComponent) {
        super(aiComponent);
    }

    @Override
    public List<Action> entryActions() {
        return null;
    }

    @Override
    public List<Action> activeActions() {
        return null;
    }

    @Override
    public List<Action> exitActions() {
        return null;
    }

    @Override
    public List<Transition> outgoingTransitions() {
        transitions.clear();
        transitions.add(new PatrolTransition(owner));
        transitions.add(new AttackTransition(owner));

        return transitions;
    }
}
