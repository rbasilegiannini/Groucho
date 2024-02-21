package com.personal.groucho.game.AI.transitions;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class IdleTransition extends Transition {
    private static IdleTransition transition = null;

    private IdleTransition() { super(); }

    public static Transition getInstance(AIComponent aiComponent) {
        if (transition == null) {
            transition = new IdleTransition();
        }
        transition.owner = aiComponent;
        return transition;
    }

    @Override
    public Condition guard() {
        return () ->
                !owner.isPlayerEngaged && !owner.isInvestigate && owner.originalState == StateName.IDLE;
    }

    @Override
    public List<Action> actions() { return Collections.emptyList(); }

    @Override
    public AIState targetState() { return Idle.getInstance(owner); }
}

