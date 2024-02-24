package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Events.enemyHitPlayerEvent;
import static com.personal.groucho.game.Utils.directionBetweenGO;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterScaleFactor;
import static com.personal.groucho.game.constants.System.maxInvisiblePlayer;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.CHARACTER;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.AI.Sight;
import com.personal.groucho.game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends WalkingComponent {
    private CharacterComponent character = null;
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
                fsm = new FSM(Patrol.getInstance(this));
                break;
            case ENGAGE:
                fsm = new FSM(Engage.getInstance(this));
                break;
            case ATTACK:
                fsm = new FSM(Attack.getInstance(this));
                break;
            default:
                fsm = new FSM(Idle.getInstance(this));
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
        init(gameWorld);

        originalPosOnGrid = grid.getNode(
                posComponent.getPosXOnGrid(),
                posComponent.getPosYOnGrid()
        );
        originalOrientation = posComponent.orientation;

        actions = fsm.getActions();
        for (Action action : actions) {
            action.doIt();
        }

        sight.updateSightPosition(posComponent.posX, posComponent.posY);
        sight.see(gameWorld);
    }

    private void init(GameWorld gameWorld) {
        initComponents();

        if (sight == null) {
            sight = new Sight(
                    this,
                    gameWorld.getWorld(),
                    new Vec2(posComponent.posX, posComponent.posY),
                    posComponent.orientation);
        }
    }

    private void initComponents() {
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }
        if (character == null) {
            character = (CharacterComponent) owner.getComponent(CHARACTER);
        }
        if (playerAliveComponent == null) {
            playerAliveComponent = (AliveComponent) gameWorld.getPlayerGO().getComponent(ALIVE);
        }
    }

    public Sight getSight() {return sight;}
    public boolean isPlayerVisible() {return gameWorld.isPlayerVisible();}
    public void setPlayerEngaged(boolean isPlayerEngaged) {this.isPlayerEngaged = isPlayerEngaged;}
    public void setInvestigateStatus(boolean isInvestigate) {this.isInvestigate = isInvestigate;}

    // Idle actions
    public void entryIdleAction() {
        if (!currentPath.isEmpty() || !isNodeReached) {
            updateSprite(character.properties.sheetWalk);
        }
        else {
            updateSprite(character.properties.sheetIdle);
        }
    }

    public void activeIdleAction() {
        if (!currentPath.isEmpty() || !isNodeReached)
            walkingToDestination();
        else if (!isIdle){
            posComponent.setOrientation(originalOrientation);
            sight.setNewOrientation(originalOrientation);
            updateSprite(character.properties.sheetIdle);
            isIdle = true;
        }
    }

    public void exitIdleAction() {
        isIdle = false;
    }

    // Patrol actions
    public void entryPatrolAction() {
        currentSteps = 0;
        updateSprite(character.properties.sheetWalk);
        posComponent.setOrientation(posComponent.orientation.getOpposite());
    }

    public void activePatrolAction() {
        initComponents();

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
        walking();
    }

    // Investigate actions
    public void entryInvestigateAction() {
        initComponents();

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
        initComponents();

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
        updateSprite(character.properties.sheetHurt);
    }

    public void activeAttackAction() {
        initComponents();

        isPlayerReached = isAPlayerNeighbor();

        if (!gameWorld.isGameOver() && playerAliveComponent.currentStatus != DEAD) {
            long delay =
                    character.properties.sheetHurt.getDelay(0) * character.properties.sheetHurt.getLength(0);
            if (isPlayerReached && System.currentTimeMillis() - lastHitMillis > delay) {
                updateDirection(directionBetweenGO(gameWorld.getPlayerGO(), (GameObject)owner));
                enemyHitPlayerEvent(playerAliveComponent, character.properties.power);
                lastHitMillis = System.currentTimeMillis();
            }
        }
        else {
            isPlayerEngaged = false;
            isPlayerReached = false;

            switch (originalState) {
                case IDLE:
                    fsm.setState(Idle.getInstance(this));
                    break;
                case PATROL:
                    fsm.setState(Patrol.getInstance(this));
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

        if (posComponent.posX != (currentNode.posX*cellSize)+0.5*cellSize) {
            walkingToXCoordinate(posComponent.posX, (int)((currentNode.posX*cellSize)+0.5*cellSize));
        }
        else if (posComponent.posY != (currentNode.posY*cellSize)+0.5*cellSize) {
            walkingToYCoordinate(posComponent.posY, (int)((currentNode.posY*cellSize)+0.5*cellSize));
        }

        if (posOnGrid.equal(currentNode)) {
            isNodeReached = true;
            currentNode = null;
        }
    }

    private void walkingToXCoordinate(int startX, int targetPosX){
        if (abs(startX - targetPosX) < abs(character.properties.speed*increaseX)) {
            phyComponent.setPosX(targetPosX);
            return;
        }

        if (startX < targetPosX) {
            posComponent.setOrientation(RIGHT);
        }
        if (startX > targetPosX) {
            posComponent.setOrientation(LEFT);
        }
        walking();
    }

    private void walkingToYCoordinate(int startY, int targetPosY){
        if (abs(startY - targetPosY) < abs(character.properties.speed*increaseY)) {
            phyComponent.setPosY(targetPosY);
            return;
        }

        if (startY < targetPosY) {
            posComponent.setOrientation(DOWN);
        }
        if (startY > targetPosY) {
            posComponent.setOrientation(UP);
        }
        walking();
    }

    @Override
    protected void walking() {
        super.walking();
        sight.updateSightPosition(posComponent.posX, posComponent.posY);
        sight.setNewOrientation(posComponent.orientation);
    }

    public void updateDirection(Orientation orientation){
        posComponent.setOrientation(orientation);
        ((SpriteDrawableComponent)
                owner.getComponent(DRAWABLE)).setAnim(posComponent.orientation.getValue());
        sight.setNewOrientation(posComponent.orientation);
    }

    public void drawDebugPath(Canvas canvas, Paint paint) {
        int startX, startY;
        for (Node node : currentPath) {
            startX = node.posX*cellSize;
            startY = node.posY*cellSize;
            canvas.drawCircle(startX, startY, 20, paint);
        }
    }
}
