package com.personal.groucho.game.AI.transitions;

import static com.personal.groucho.game.controller.states.StateName.PATROL;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class PatrolTransition extends Transition {
    private static PatrolTransition transition = null;

    private PatrolTransition() {
        super();
    }

    public static Transition getInstance(AIComponent aiComponent) {
        if (transition == null) {
            transition = new PatrolTransition();
        }
        transition.owner = aiComponent;
        return transition;
    }

    @Override
    public Condition guard() {
        return () ->
                !owner.isPlayerEngaged && !owner.investigateActions.isInvestigate && owner.originalState == PATROL;
    }

    @Override
    public List<Action> actions() {
        return Collections.emptyList();
    }

    @Override
    public AIState targetState() {return owner.patrol;}
}
