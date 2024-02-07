package com.personal.groucho.game.AI.transitions;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Attack;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class AttackTransition extends Transition {
    public AttackTransition(AIComponent aiComponent) {
        super(aiComponent);
    }

    @Override
    public Condition guard() {
        return new Condition() {
            @Override
            public boolean eval() {
                return owner.isPlayerReached();
            }
        };
    }

    @Override
    public List<Action> actions() {
        return Collections.emptyList();
    }

    @Override
    public State targetState() {
        return new Attack(owner);
    }
}
