package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Events.enemyHitPlayerEvent;
import static com.personal.groucho.game.Utils.directionBetweenGO;
import static com.personal.groucho.game.constants.CharacterProperties.skeletonPower;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.CharacterProperties.skeletonSpeed;
import static com.personal.groucho.game.assets.Spritesheets.skeletonHurt;
import static com.personal.groucho.game.assets.Spritesheets.skeletonIdle;
import static com.personal.groucho.game.assets.Spritesheets.skeletonWalk;
import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterScaleFactor;
import static com.personal.groucho.game.constants.System.maxInvisiblePlayer;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

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
import com.personal.groucho.game.AI.Sight;
import com.personal.groucho.game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends WalkingComponent {
    public final StateName originalState;
    private final FSM fsm;
    private final GameGrid grid;
    private Sight sight = null;
    private final GameWorld gameWorld;
    private final AStar aStar;
    private Orientation originalOrientation;
    private Node originalPosOnGrid;
    private Node posOnGrid;
    private Node playerPosOnGrid;
    private Node currentNode;
    private final int maxSteps;
    private int currentSteps;
    private long lastHitMillis;
    private boolean isNodeReached = true;
    public boolean isPlayerEngaged = false;
    public boolean isPlayerReached = false;
    public boolean isInvestigate = false;
    private boolean isIdle = false;
    private boolean isPatrol = false;
    private long lastSeenMills;

    // To avoid further allocations
    private List<Action> actions;
    private List<Node> currentPath;
    private AliveComponent playerAliveComponent = null;


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
    public ComponentType type() { return AI; }

    public void update(GameWorld gameWorld) {
        if (sight == null) {
            init(gameWorld);
        }

        actions = fsm.getActions();
        for (Action action : actions) {
            action.doIt();
        }

        sight.updateSightPosition(posComponent.posX, posComponent.posY);
        sight.see(gameWorld);
    }

    private void init(GameWorld gameWorld) {
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }

        originalPosOnGrid = grid.getNode(
                posComponent.getPosXOnGrid(),
                posComponent.getPosYOnGrid()
        );
        originalOrientation = posComponent.orientation;

        sight = new Sight(
                this,
                gameWorld.getWorld(),
                new Vec2(posComponent.posX, posComponent.posY),
                posComponent.orientation);
    }

    public Sight getSight() {return sight;}
    public boolean isPlayerVisible() {return gameWorld.isPlayerVisible();}
    public void setPlayerEngaged(boolean isPlayerEngaged) {this.isPlayerEngaged = isPlayerEngaged;}
    public void setInvestigateStatus(boolean isInvestigate) {this.isInvestigate = isInvestigate;}

    // Idle actions
    public void entryIdleAction() {
        if (!currentPath.isEmpty() || !isNodeReached) {
            updateSprite(skeletonWalk);
        }
        else {
            updateSprite(skeletonIdle);
        }
    }

    public void activeIdleAction() {
        if (!currentPath.isEmpty() || !isNodeReached)
            walkingToDestination();
        else if (!isIdle){
            posComponent.setOrientation(originalOrientation);
            sight.setNewOrientation(originalOrientation);
            updateSprite(skeletonIdle);
            isIdle = true;
        }
    }

    public void exitIdleAction() {
        isIdle = false;
    }

    // Patrol actions
    public void entryPatrolAction() {
        updateSprite(skeletonWalk);
        posComponent.setOrientation(posComponent.orientation.getOpposite());
    }

    public void activePatrolAction() {
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }
        if (!currentPath.isEmpty() || !isNodeReached)
            walkingToDestination();
        else {
            if (!isPatrol) {
                posComponent.setOrientation(originalOrientation);
                sight.setNewOrientation(originalOrientation);
                isPatrol = true;
            }
            patrol();
        }
    }

    public void exitPatrolAction() {
        isPatrol = false;
        currentSteps = 0;
    }

    private void patrol() {
        if (currentSteps == maxSteps) {
            currentSteps = 0;
            posComponent.setOrientation(posComponent.orientation.getOpposite());
        }
        currentSteps++;
        walking(skeletonWalk, skeletonSpeed);
    }

    // Investigate actions
    public void entryInvestigateAction() {
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }
        posOnGrid = grid.getNode(
                posComponent.getPosXOnGrid(),
                posComponent.getPosYOnGrid()
        );
        setPathToPlayer();
        isNodeReached = true;
        isInvestigate = true;
    }

    public void activeInvestigateAction() {
        if (!currentPath.isEmpty() || !isNodeReached) {
            walkingToDestination();
        }
        else {
            isInvestigate = false;
        }
    }

    public void exitInvestigateAction() {
        isInvestigate = false;
        currentPath.clear();
        currentPath = aStar.findPath(posOnGrid, originalPosOnGrid);
    }

    // Engage actions
    public void entryEngageAction(){
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }
        posOnGrid = grid.getNode(
                posComponent.getPosXOnGrid(),
                posComponent.getPosYOnGrid()
        );
        lastSeenMills = System.currentTimeMillis();
        setPathToPlayer();
        isNodeReached = true;
    }

    public void activeEngageAction(){
        if (hasPlayerChangedPosition() && currentNode == null) {
            setPathToPlayer();
        }
        isPlayerReached = isAPlayerNeighbor();

        if (!currentPath.isEmpty() || !isNodeReached) {
            walkingToDestination();
        }
        else {
            isPlayerReached = false;
        }

        if (!gameWorld.isPlayerVisible()) {
            if (System.currentTimeMillis() - lastSeenMills > maxInvisiblePlayer){
                isPlayerEngaged = false;
            }
        }
        else {
            lastSeenMills = System.currentTimeMillis();
        }
    }

    public void exitEngageAction() {
        currentPath.clear();
        currentPath = aStar.findPath(posOnGrid, originalPosOnGrid);
    }

    private boolean hasPlayerChangedPosition() {
        return !playerPosOnGrid.equal(grid.getNode(
                (int) gameWorld.getPlayerPositionX() / cellSize,
                (int) gameWorld.getPlayerPositionY() / cellSize));
    }

    // Attack actions
    public void entryAttackAction() {
        lastHitMillis = System.currentTimeMillis();
        sight.setNewOrientation(posComponent.orientation);
        updateSprite(skeletonHurt);
    }

    public void activeAttackAction() {
        if (playerAliveComponent == null) {
            playerAliveComponent = (AliveComponent) gameWorld.getPlayerGO().getComponent(ALIVE);
        }
        isPlayerReached = isAPlayerNeighbor();

        if (!gameWorld.isGameOver() && playerAliveComponent.currentStatus != DEAD) {
            long delay = skeletonHurt.getDelay(0) * skeletonHurt.getLength(0);
            if (isPlayerReached && System.currentTimeMillis() - lastHitMillis > delay) {
                updateDirection(directionBetweenGO(gameWorld.getPlayerGO(), (GameObject)owner));
                enemyHitPlayerEvent(playerAliveComponent, skeletonPower);
                lastHitMillis = System.currentTimeMillis();
            }
        }
        else {
            isPlayerEngaged = false;
            isPlayerReached = false;

            switch (originalState) {
                case IDLE:
                    fsm.setState(new Idle(this));
                    break;
                case PATROL:
                    fsm.setState(new Patrol(this));
                    break;
            }
        }
    }

    public void exitAttackAction() {
        isPlayerReached = false;
    }

    private void setPathToPlayer() {
        posOnGrid = grid.getNode(
                posComponent.getPosXOnGrid(),
                posComponent.getPosYOnGrid()
        );

        playerPosOnGrid = grid.getNode(
                (int) gameWorld.getPlayerPositionX()/cellSize,
                (int) gameWorld.getPlayerPositionY()/cellSize
        );

        currentPath.clear();
        currentPath = aStar.findPath(posOnGrid, playerPosOnGrid);
    }

    // TODO: Optimize this method
    public boolean isAPlayerNeighbor() {
        float distanceFromPlayerX =
                (float) posComponent.posX - gameWorld.getPlayerPositionX();
        float distanceFromPlayerY =
                (float) posComponent.posY - gameWorld.getPlayerPositionY();

        float distanceFromPlayer = (float)sqrt(pow(distanceFromPlayerX,2) + pow(distanceFromPlayerY,2));

        return distanceFromPlayer < 1.2*characterScaleFactor* characterDimX;
    }

    private void walkingToDestination() {
        if (isNodeReached || currentNode == null) {
            currentNode = currentPath.remove(0);
            isNodeReached = false;
        }

        posOnGrid = grid.getNode(
                posComponent.getPosXOnGrid(),
                posComponent.getPosYOnGrid()
        );

        if (posOnGrid.posX != currentNode.posX) {
            walkingToXCoordinate(posOnGrid.posX, currentNode.posX);
        }
        else if (posOnGrid.posY != currentNode.posY) {
            walkingToYCoordinate(posOnGrid.posY, currentNode.posY);
        }

        if (posOnGrid.equal(currentNode)) {
            isNodeReached = true;
            currentNode = null;
        }
    }

    private void walkingToXCoordinate(int startX, int targetPosX){
        if (startX < targetPosX) {
            posComponent.setOrientation(RIGHT);
        }
        if (startX > targetPosX) {
            posComponent.setOrientation(LEFT);
        }
        walking(skeletonWalk, skeletonSpeed);
    }

    private void walkingToYCoordinate(int startY, int targetPosY){
        if (startY < targetPosY) {
            posComponent.setOrientation(DOWN);
        }
        if (startY > targetPosY) {
            posComponent.setOrientation(UP);
        }
        walking(skeletonWalk, skeletonSpeed);
    }

    @Override
    protected void walking(Spritesheet sheet, float speed) {
        super.walking(sheet, speed);
        sight.updateSightPosition(posComponent.posX, posComponent.posY);
        sight.setNewOrientation(posComponent.orientation);
    }

    public void updateDirection(Orientation orientation){
        posComponent.setOrientation(orientation);
        ((SpriteDrawableComponent)
                owner.getComponent(DRAWABLE)).setAnim(posComponent.orientation.getValue());
        sight.setNewOrientation(posComponent.orientation);
    }
}
