package com.personal.groucho.game.AI.transitions;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Attack;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class AttackTransition extends Transition {
    private static AttackTransition transition = null;

    private AttackTransition() {
        super();
    }

    public static Transition getInstance(AIComponent aiComponent) {
        if (transition == null) {
            transition = new AttackTransition();
        }
        transition.owner = aiComponent;
        return transition;
    }

    @Override
    public Condition guard() {
        return () -> owner.isPlayerReached;
    }

    @Override
    public List<Action> actions() {
        return Collections.emptyList();
    }

    @Override
    public AIState targetState() {
        return Attack.getInstance(owner);
    }
}
