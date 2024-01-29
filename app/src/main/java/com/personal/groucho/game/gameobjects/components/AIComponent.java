package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.skeletonSpeed;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_idle;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_walk;

import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.FSM;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.controller.Orientation;

import java.util.List;

public class AIComponent extends Component{

    private Orientation orientation;
    private final int maxSteps;
    private int currentSteps;
    private final FSM fsm;
    private PhysicsComponent physicsComponent = null;

    public AIComponent() {
        fsm = new FSM(new Idle(this));
        orientation = Orientation.UP;
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

    public void entryPatrolAction() {
        updateSprite(skeleton_walk);
    }

    public void activePatrolAction() {
        if(physicsComponent == null)
            physicsComponent = (PhysicsComponent) owner.getComponent(ComponentType.Physics);

        if (currentSteps == maxSteps) {
            currentSteps = 0;
            orientation = orientation.getOpposite();
        }

        updateSprite(skeleton_walk);

        switch (orientation) {
            case UP:
                updatePosition(0, skeletonSpeed *(-1));
                break;
            case DOWN:
                updatePosition(0, skeletonSpeed *(1));
                break;
            case LEFT:
                updatePosition(skeletonSpeed *(-1),0);
                break;
            case RIGHT:
                updatePosition(skeletonSpeed *(1),0);
                break;
        }
        currentSteps++;
    }

    private void updateSprite(Spritesheet sheet) {
        Component component = owner.getComponent(ComponentType.Drawable);
        if (component != null) {
            SpriteDrawableComponent spriteComponent = (SpriteDrawableComponent) component;
            spriteComponent.setCurrentSpritesheet(sheet);
            spriteComponent.setAnimation(orientation.getValue());
        }
    }

    private void updatePosition(float increaseX, float increaseY) {
        physicsComponent.updatePosX(increaseX);
        physicsComponent.updatePosY(increaseY);
    }

    public void updateOrientation(Orientation orientation) {this.orientation = orientation;}
    public Orientation getOrientation() {return orientation;}
}
