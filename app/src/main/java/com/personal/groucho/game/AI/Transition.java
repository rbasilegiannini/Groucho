package com.personal.groucho.game.AI;
import com.personal.groucho.game.gameobjects.components.AIComponent;

import java.util.List;

public abstract class Transition {
    protected AIComponent owner;

    public abstract Condition guard();
    public abstract List<Action> actions();
    public abstract AIState targetState();
}
