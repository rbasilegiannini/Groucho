package com.personal.groucho.game.AI.transitions;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Engage;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class EngageTransition extends Transition {
    private static EngageTransition transition = null;

    private EngageTransition(AIComponent aiComponent) {
        super(aiComponent);
    }

    public static Transition getInstance(AIComponent aiComponent) {
        if (transition == null) {
            transition = new EngageTransition(aiComponent);
        }
        return transition;
    }

    @Override
    public Condition guard() {
        return () -> owner.isPlayerEngaged && !owner.isPlayerReached;
    }

    @Override
    public List<Action> actions() {
        return Collections.emptyList();
    }

    @Override
    public AIState targetState() {
        return Engage.getInstance(owner);
    }
}
