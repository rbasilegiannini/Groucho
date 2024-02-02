package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.skeletonSpeed;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_idle;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_walk;

import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.FSM;
import com.personal.groucho.game.AI.pathfinding.AStar;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.Sight;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends WalkingComponent {
    private final int maxSteps;
    private int currentSteps;
    private final FSM fsm;
    private Sight sight = null;
    private final GameWorld gameWorld;
    private final World world;
    private final AStar aStar;
    private final Node positionOnGrid;
    private List<Node> currentPath;
    private Node current;
    private boolean newNode = true;


    public AIComponent(GameWorld gameWorld, GameGrid grid) {
        this.gameWorld = gameWorld;
        this.world = gameWorld.getWorld();
        fsm = new FSM(new Idle(this));
        aStar = new AStar(grid);
        positionOnGrid = new Node(0,0);
        currentPath = new ArrayList<>();
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
                    new Vec2(positionComponent.getPosX(),positionComponent.getPosY()));
        }
        List<Action> actions = fsm.getActions(gameWorld);
        for (Action action : actions)
            action.doIt();

        sight.see();
    }

    //
    public Sight getSight() {return sight;}
    //

    public void entryIdleAction() {updateSprite(skeleton_idle);}

    public void entryPatrolAction() { updateSprite(skeleton_walk); }

    public void activePatrolAction() {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        if (currentSteps == maxSteps) {
            currentSteps = 0;
            Orientation newOrientation = positionComponent.getOrientation().getOpposite();
            positionComponent.setOrientation(newOrientation);
        }
        currentSteps++;

        walking(skeleton_walk, skeletonSpeed);
        sight.updateSight(positionComponent.getPosition());
        sight.setNewOrientation(positionComponent.getOrientation());
    }

    public void entryEngageAction(){
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);
        positionOnGrid.setPosX(positionComponent.getPosX()/64);
        positionOnGrid.setPosY(positionComponent.getPosY()/64);

        Node playerPositionOnGrid = new Node(
                (int) gameWorld.getPlayerPosition().getX()/64,
                (int) gameWorld.getPlayerPosition().getY()/64
        );

        currentPath.clear();
        currentPath = aStar.findPath(positionOnGrid, playerPositionOnGrid);

        // if (currentPath.isEmpty()) guard to comeback in patrol
    }

    public void activeEngageAction(){
        if (!currentPath.isEmpty() || !newNode) {
            if (newNode) {
                current = currentPath.remove(0);
                newNode = false;
            }

            if (positionOnGrid.posX != current.posX) {
                walkingToXCoordinate(current.getPosX());
            } else if (positionOnGrid.posY != current.posY) {
                walkingToYCoordinate(current.getPosY());
            }

            if (positionOnGrid.posX == current.posX && positionOnGrid.posY == current.posY) {
                newNode = true;
            }
        }
        else
            spriteComponent.setCurrentSpritesheet(skeleton_idle);
    }

    private void walkingToXCoordinate(int targetPosX){
        positionOnGrid.setPosX(positionComponent.getPosX()/64);
        int positionOnGridX = positionOnGrid.getPosX();

        if (positionOnGridX != targetPosX) {
            if (positionOnGridX < targetPosX) {
                positionComponent.setOrientation(Orientation.RIGHT);
            }
            if (positionOnGridX >= targetPosX) {
                positionComponent.setOrientation(Orientation.LEFT);
            }
            walking(skeleton_walk, skeletonSpeed);
            sight.updateSight(positionComponent.getPosition());
            sight.setNewOrientation(positionComponent.getOrientation());
        }
    }

    private void walkingToYCoordinate(int targetPosY){
        positionOnGrid.setPosY(positionComponent.getPosY()/64);
        int positionOnGridY = positionOnGrid.getPosY();

        if (positionOnGridY != targetPosY) {
            if (positionOnGridY < targetPosY) {
                positionComponent.setOrientation(Orientation.DOWN);
            }
            if (positionOnGridY >= targetPosY) {
                positionComponent.setOrientation(Orientation.UP);
            }
            walking(skeleton_walk, skeletonSpeed);
            sight.updateSight(positionComponent.getPosition());
            sight.setNewOrientation(positionComponent.getOrientation());
        }
    }
}
