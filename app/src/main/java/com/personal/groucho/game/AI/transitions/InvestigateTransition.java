package com.personal.groucho.game.AI.transitions;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.Condition;
import com.personal.groucho.game.AI.AIState;
import com.personal.groucho.game.AI.Transition;
import com.personal.groucho.game.AI.states.Investigate;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.Collections;
import java.util.List;

public class InvestigateTransition extends Transition {
    private static InvestigateTransition transition = null;

    private InvestigateTransition() {super();}

    public static Transition getInstance(AIComponent aiComponent) {
        if (transition == null) {
            transition = new InvestigateTransition();
        }
        transition.owner = aiComponent;
        return transition;
    }

    @Override
    public Condition guard() {return () -> owner.isInvestigate;}

    @Override
    public List<Action> actions() { return Collections.emptyList(); }

    @Override
    public AIState targetState() { return Investigate.getInstance(owner); }
}
