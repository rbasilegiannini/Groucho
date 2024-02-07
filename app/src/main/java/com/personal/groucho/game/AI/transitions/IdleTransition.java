package com.personal.groucho.game.AI.transitions;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class IdleTransition extends Transition {

    public IdleTransition(AIComponent aiComponent) { super(aiComponent); }

    @Override
    public Condition guard() {
        return new Condition() {
            @Override
            public boolean eval(GameWorld gameWorld) {
                return !gameWorld.isPlayerEngaged() && owner.wasIdle();
            }
        };
    }

    @Override
    public List<Action> actions() { return Collections.emptyList(); }

    @Override
    public State targetState() { return new Idle(owner); }
}

