package com.personal.groucho.game.AI;

import java.util.List;

public interface State {
    List<Action> entryActions();
    List<Action> activeActions();
    List<Action> exitActions();
    List<Transition> outgoingTransitions();
}
