package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.skeletonSpeed;
import static com.personal.groucho.game.Utils.toPixelsXLength;
import static com.personal.groucho.game.Utils.toPixelsYLength;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_idle;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_walk;


import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.FSM;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.Sight;

import java.util.List;

public class AIComponent extends WalkingComponent {
    private final int maxSteps;
    private int currentSteps;
    private final FSM fsm;
    private Sight sight = null;
    private final World world;

    public AIComponent(World world) {
        this.world = world;
        fsm = new FSM(new Idle(this));

        currentSteps = 0;
        maxSteps = 1000;
    }

    @Override
    public ComponentType type() { return ComponentType.AI; }

    public void update(GameWorld gameWorld) {
        if (sight == null) {
            if (positionComponent == null)
                positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

            sight = new Sight(
                    world,
                    new Vec2(positionComponent.getPosX(),positionComponent.getPosY()),
                    500);
        }
        List<Action> actions = fsm.getActions(gameWorld);
        for (Action action : actions)
            action.doIt();

        sight.see();
    }

    //
    public Sight getSight() {return sight;}
    //

    public void entryIdleAction() {
        updateSprite(skeleton_idle);
    }

    public void entryPatrolAction() { updateSprite(skeleton_walk); }

    public void activePatrolAction() {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        if (currentSteps == maxSteps) {
            currentSteps = 0;
            Orientation newOrientation = positionComponent.getOrientation().getOpposite();
            positionComponent.setOrientation(newOrientation);
        }

        walking(skeleton_walk, skeletonSpeed);
        sight.updateSight(
                toPixelsXLength(skeletonSpeed*increaseX),
                toPixelsYLength(skeletonSpeed*increaseY)
        );
        sight.setNewOrientation(positionComponent.getOrientation());
        currentSteps++;
    }
}
