package com.personal.groucho.game.AI.transitions;

import static com.personal.groucho.game.AI.states.StateName.PATROL;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.State;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Patrol;
import com.personal.groucho.game.AI.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class PatrolTransition extends Transition {

    public PatrolTransition(AIComponent aiComponent) {
        super(aiComponent);
    }

    @Override
    public Condition guard() {
        return () ->
                !owner.isPlayerEngaged && !owner.isInvestigate && owner.originalState == PATROL;
    }

    @Override
    public List<Action> actions() {
        return Collections.emptyList();
    }

    @Override
    public State targetState() {
        return new Patrol(owner);
    }
}
