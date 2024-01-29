package com.personal.groucho.game.AI;

import java.util.List;

public interface Transition {
    Condition guard();
    List<Action> actions();
    State targetState();
}
