package com.personal.groucho.game.AI.transitions;

import static com.personal.groucho.game.AI.states.StateName.IDLE;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.AI.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class IdleTransition extends Transition {

    public IdleTransition(AIComponent aiComponent) { super(aiComponent); }

    @Override
    public Condition guard() {
        return () ->
                !owner.isPlayerEngaged && !owner.isInvestigate && owner.originalState == IDLE;
    }

    @Override
    public List<Action> actions() { return Collections.emptyList(); }

    @Override
    public State targetState() { return new Idle(owner); }
}

