package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.assets.Spritesheets.skeleton_idle;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_walk;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.FSM;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.ComponentType;

import java.util.List;

public class AIComponent extends WalkingComponent {
    private final int maxSteps;
    private int currentSteps;
    private final FSM fsm;

    public AIComponent() {
        fsm = new FSM(new Idle(this));
        currentSteps = 0;
        maxSteps = 1000;
    }

    @Override
    public ComponentType type() { return ComponentType.AI; }

    public void update(GameWorld gameWorld) {
        List<Action> actions = fsm.getActions(gameWorld);
        for (Action action : actions)
            action.doIt();
    }

    public void entryIdleAction() {
        updateSprite(skeleton_idle);
    }

    public void entryPatrolAction() { updateSprite(skeleton_walk); }

    public void activePatrolAction() {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        if (currentSteps == maxSteps) {
            currentSteps = 0;
            positionComponent.setOrientation(positionComponent.getOrientation().getOpposite());
        }

        walking(skeleton_walk);
        currentSteps++;
    }
}
