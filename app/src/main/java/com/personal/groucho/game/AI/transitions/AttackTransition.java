package com.personal.groucho.game.AI.transitions;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public class AttackTransition extends Transition {
    public AttackTransition(AIComponent aiComponent) {
        super(aiComponent);
    }

    @Override
    public Condition guard() {
        return new Condition() {
            @Override
            public boolean eval(GameWorld gameWorld) {
                return false;
            }
        };
    }

    @Override
    public List<Action> actions() {
        return null;
    }

    @Override
    public State targetState() {
        return null;
    }
}
