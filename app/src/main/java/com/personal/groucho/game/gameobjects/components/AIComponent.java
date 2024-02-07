package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.CharacterProperties.skeletonSpeed;
import static com.personal.groucho.game.assets.Spritesheets.skeletonHurt;
import static com.personal.groucho.game.assets.Spritesheets.skeletonIdle;
import static com.personal.groucho.game.assets.Spritesheets.skeletonWalk;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.FSM;
import com.personal.groucho.game.AI.pathfinding.AStar;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.AI.states.Attack;
import com.personal.groucho.game.AI.states.Engage;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.AI.states.Patrol;
import com.personal.groucho.game.AI.states.StateName;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.Sight;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends WalkingComponent {
    private final StateName originalState;
    private final FSM fsm;
    private final GameGrid grid;
    private Sight sight = null;
    private final GameWorld gameWorld;
    private final AStar aStar;
    private Orientation originalOrientation;
    private Node originalPositionOnGrid;
    private Node positionOnGrid;
    private Node playerPositionOnGrid;
    private List<Node> currentPath;
    private Node currentNode;
    private final int maxSteps;
    private int currentSteps;
    private boolean isNodeReached = true;
    private boolean isPlayerEngaged = false;
    private boolean isPlayerReached = false;
    private boolean isIdle = false;

    public AIComponent(GameWorld gameWorld, StateName currentState) {
        this.gameWorld = gameWorld;
        grid = gameWorld.getGameGrid();

        originalState = currentState;
        switch (originalState) {
            case PATROL:
                fsm = new FSM(new Patrol(this));
                break;
            case ENGAGE:
                fsm = new FSM(new Engage(this));
                break;
            case ATTACK:
                fsm = new FSM(new Attack(this));
                break;
            default:
                fsm = new FSM(new Idle(this));
                break;
        }

        aStar = new AStar(grid);
        currentPath = new ArrayList<>();
        currentSteps = 0;
        maxSteps = 100;
    }

    @Override
    public ComponentType type() { return ComponentType.AI; }

    public void update(GameWorld gameWorld) {
        if (sight == null) {
            if (positionComponent == null)
                positionComponent = (PositionComponent) owner.getComponent(ComponentType.POSITION);

            sight = new Sight(
                    this,
                    gameWorld.getWorld(),
                    new Vec2(positionComponent.getPosX(),positionComponent.getPosY()));
        }
        List<Action> actions = fsm.getActions();
        for (Action action : actions)
            action.doIt();

        sight.see();
    }

    public Sight getSight() {return sight;}
    public StateName getOriginalState(){return originalState;}
    public boolean isPlayerEngaged() {return isPlayerEngaged;}
    public boolean isPlayerReached() {return isPlayerReached;}
    public void setPlayerEngaged(boolean isPlayerEngaged) {this.isPlayerEngaged = isPlayerEngaged;}

    // Idle actions
    public void entryIdleAction() {
        if (!currentPath.isEmpty() || !isNodeReached)
            updateSprite(skeletonWalk);
        else
            updateSprite(skeletonIdle);
    }

    public void activeIdleAction() {
        if (!currentPath.isEmpty() || !isNodeReached)
            walkingToDestination();
        else if (!isIdle){
            if (originalOrientation != null) {
                positionComponent.setOrientation(originalOrientation);
                sight.setNewOrientation(originalOrientation);
                originalOrientation = null;
            }
            updateSprite(skeletonIdle);
            isIdle = true;
        }
    }

    public void exitIdleAction() {
        originalOrientation = positionComponent.getOrientation();
        isIdle = false;
    }

    // Patrol actions
    public void entryPatrolAction() {
        updateSprite(skeletonWalk);
        Orientation newOrientation = positionComponent.getOrientation().getOpposite();
        positionComponent.setOrientation(newOrientation);
    }

    public void activePatrolAction() {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.POSITION);

        if (!currentPath.isEmpty() || !isNodeReached)
            walkingToDestination();
        else {
            if (originalOrientation != null) {
                positionComponent.setOrientation(originalOrientation);
                sight.setNewOrientation(originalOrientation);
                originalOrientation = null;
            }
            patrol();
        }
    }

    public void exitPatrolAction() {
        originalOrientation = positionComponent.getOrientation();
        currentSteps = 0;
    }

    private void patrol() {
        if (currentSteps == maxSteps) {
            currentSteps = 0;
            Orientation newOrientation = positionComponent.getOrientation().getOpposite();
            positionComponent.setOrientation(newOrientation);
        }
        currentSteps++;
        walking(skeletonWalk, skeletonSpeed);
    }

    // Engage actions
    public void entryEngageAction(){
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.POSITION);

        positionOnGrid = grid.getNode(
                positionComponent.getPositionXOnGrid(),
                positionComponent.getPositionYOnGrid()
        );
        originalPositionOnGrid = positionOnGrid;

        setPathToPlayer();
        isNodeReached = true;
    }

    public void activeEngageAction(){
        if (hasPlayerChangedPosition())
            setPathToPlayer();

        isPlayerReached = isAPlayerNeighbor(positionOnGrid);

        if (!currentPath.isEmpty() || !isNodeReached) {
            walkingToDestination();
        }
        else {
            updateSprite(skeletonIdle);
            isPlayerReached = false;
        }
    }

    public void exitEngageAction() {
        currentPath.clear();
        currentPath = aStar.findPath(positionOnGrid, originalPositionOnGrid);
    }

    private boolean hasPlayerChangedPosition() {
        return !playerPositionOnGrid.equal(grid.getNode(
                (int) gameWorld.getPlayerPosition().getX() / cellSize,
                (int) gameWorld.getPlayerPosition().getY() / cellSize));
    }

    // Attack actions
    public void entryAttackAction() {
        sight.setNewOrientation(positionComponent.getOrientation());
        updateSprite(skeletonHurt);
    }

    public void activeAttackAction() {
        isPlayerReached = isAPlayerNeighbor(positionOnGrid);
    }

    private void setPathToPlayer() {
        playerPositionOnGrid = grid.getNode(
                (int) gameWorld.getPlayerPosition().getX()/cellSize,
                (int) gameWorld.getPlayerPosition().getY()/cellSize
        );

        currentPath.clear();
        currentPath = aStar.findPath(positionOnGrid, playerPositionOnGrid);
    }

    public boolean isAPlayerNeighbor(Node nodeOnGrid) {
        Node playerOnGrid = grid.getNode(
                (int)gameWorld.getPlayerPosition().getX()/cellSize,
                (int)gameWorld.getPlayerPosition().getY()/cellSize
        );

        List<Node> playerNeighbors = grid.getNeighbors(playerOnGrid);

        return playerNeighbors.contains(nodeOnGrid);
    }

    private void walkingToDestination() {
        if (isNodeReached || currentNode == null) {
            currentNode = currentPath.remove(0);
            isNodeReached = false;
        }

        positionOnGrid = grid.getNode(
                positionComponent.getPositionXOnGrid(),
                positionComponent.getPositionYOnGrid()
        );

        if (positionOnGrid.getPosX() != currentNode.getPosX())
            walkingToXCoordinate(currentNode.getPosX());

        else if (positionOnGrid.getPosY() != currentNode.getPosY())
            walkingToYCoordinate(currentNode.getPosY());

        if (positionOnGrid.equal(currentNode)) {
            isNodeReached = true;
            currentNode = null;
        }
    }

    private void walkingToXCoordinate(int targetPosX){
        int positionOnGridX = positionOnGrid.getPosX();

        if (positionOnGridX != targetPosX) {
            if (positionOnGridX < targetPosX) {
                positionComponent.setOrientation(Orientation.RIGHT);
            }
            if (positionOnGridX > targetPosX) {
                positionComponent.setOrientation(Orientation.LEFT);
            }
            walking(skeletonWalk, skeletonSpeed);
        }
    }

    private void walkingToYCoordinate(int targetPosY){
        int positionOnGridY = positionOnGrid.getPosY();

        if (positionOnGridY != targetPosY) {
            if (positionOnGridY < targetPosY) {
                positionComponent.setOrientation(Orientation.DOWN);
            }
            if (positionOnGridY > targetPosY) {
                positionComponent.setOrientation(Orientation.UP);
            }
            walking(skeletonWalk, skeletonSpeed);
        }
    }

    @Override
    protected void walking(Spritesheet sheet, float speed) {
        super.walking(sheet, speed);
        sight.updateSight(positionComponent.getPosition());
        sight.setNewOrientation(positionComponent.getOrientation());
    }
}
