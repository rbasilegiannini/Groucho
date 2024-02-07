package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.cellSize;
import static com.personal.groucho.game.Constants.skeletonSpeed;
import static com.personal.groucho.game.assets.Spritesheets.skeletonHurt;
import static com.personal.groucho.game.assets.Spritesheets.skeletonIdle;
import static com.personal.groucho.game.assets.Spritesheets.skeletonWalk;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.FSM;
import com.personal.groucho.game.AI.pathfinding.AStar;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.Sight;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends WalkingComponent {
    private final int maxSteps;
    private int currentSteps;
    private final FSM fsm;
    private final GameGrid grid;
    private Sight sight = null;
    private final GameWorld gameWorld;
    private final AStar aStar;
    private Node originalPositionOnGrid;
    private Node positionOnGrid;
    private Node playerPositionOnGrid;
    private List<Node> currentPath;
    private Node current;
    //TODO: Change "newNode" name.
    private boolean newNode = true;
    private boolean wasIdle = false;
    private boolean wasPatrol = false;
    private boolean isPlayerEngaged = false;
    private boolean isPlayerReached = false;

    public AIComponent(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.grid = gameWorld.getGameGrid();
        fsm = new FSM(new Idle(this));
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
                positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

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

    //
    public Sight getSight() {return sight;}
    //

    public void entryIdleAction() {
        if (!currentPath.isEmpty() || !newNode)
            updateSprite(skeletonWalk);
        else
            updateSprite(skeletonIdle);
    }

    public void activeIdleAction() {
        if (!currentPath.isEmpty() || !newNode)
            walkingToDestination();
        else
            // TODO: updateSprite for each call
            updateSprite(skeletonIdle);
    }

    public void exitIdleAction() {
        wasIdle = true;
        wasPatrol = false;
    }

    public void entryPatrolAction() {
        updateSprite(skeletonWalk);
        Orientation newOrientation = positionComponent.getOrientation().getOpposite();
        positionComponent.setOrientation(newOrientation);
    }

    public void activePatrolAction() {
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        if (!currentPath.isEmpty() || !newNode)
            walkingToDestination();
        else
            patrol();
    }

    public void exitPatrolAction() {
        wasIdle = false;
        wasPatrol = true;
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

    public void entryEngageAction(){
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        positionOnGrid = gameWorld.getGameGridNode(
                positionComponent.getPositionXOnGrid(),
                positionComponent.getPositionYOnGrid()
        );

        originalPositionOnGrid = positionOnGrid;

        setPathToPlayer();
        newNode = true;
    }

    public void activeEngageAction(){
        if (hasPlayerChangedPosition())
            setPathToPlayer();

        isPlayerReached = isAPlayerNeighbor(positionOnGrid);

        if (!currentPath.isEmpty() || !newNode) {
            walkingToDestination();
        }
        else {
            updateSprite(skeletonIdle);
            isPlayerReached = false;
        }
    }

    private void walkingToDestination() {
        if (newNode) {
            current = currentPath.remove(0);
            newNode = false;
        }

        positionOnGrid = gameWorld.getGameGridNode(
                positionComponent.getPositionXOnGrid(),
                positionComponent.getPositionYOnGrid()
        );

        if (positionOnGrid.getPosX() != current.getPosX())
            walkingToXCoordinate(current.getPosX());

        else if (positionOnGrid.getPosY() != current.getPosY())
            walkingToYCoordinate(current.getPosY());

        if (positionOnGrid.equal(current))
            newNode = true;
    }

    public void exitEngageAction() {
        currentPath = aStar.findPath(positionOnGrid, originalPositionOnGrid);
    }

    private boolean hasPlayerChangedPosition() {
        return !playerPositionOnGrid.equal(gameWorld.getGameGridNode(
                (int) gameWorld.getPlayerPosition().getX() / cellSize,
                (int) gameWorld.getPlayerPosition().getY() / cellSize));
    }

    public void entryAttackAction() {
        sight.setNewOrientation(positionComponent.getOrientation());
        updateSprite(skeletonHurt);
    }

    public void activeAttackAction() {
        isPlayerReached = isAPlayerNeighbor(positionOnGrid);
    }

    private void setPathToPlayer() {
        playerPositionOnGrid = gameWorld.getGameGridNode(
                (int) gameWorld.getPlayerPosition().getX()/cellSize,
                (int) gameWorld.getPlayerPosition().getY()/cellSize
        );

        currentPath.clear();
        currentPath = aStar.findPath(positionOnGrid, playerPositionOnGrid);
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

    public boolean wasIdle() {
        return wasIdle;
    }

    public boolean wasPatrol() {
        return wasPatrol;
    }

    //

    public boolean isAPlayerNeighbor(Node nodeOnGrid) {
        Node playerOnGrid = grid.getNode(
                (int)gameWorld.getPlayerPosition().getX()/cellSize,
                (int)gameWorld.getPlayerPosition().getY()/cellSize
        );

        List<Node> playerNeighbors = grid.getNeighbors(playerOnGrid);

        return playerNeighbors.contains(nodeOnGrid);
    }

    public boolean isPlayerEngaged() {
        return isPlayerEngaged;
    }

    public void setPlayerEngaged(boolean isPlayerEngaged) {
        this.isPlayerEngaged = isPlayerEngaged;
    }

    public boolean isPlayerReached() {return isPlayerReached;}

}
