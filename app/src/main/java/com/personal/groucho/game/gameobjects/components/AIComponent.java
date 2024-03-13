package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.controller.Orientation.DOWN;
import static com.personal.groucho.game.controller.Orientation.LEFT;
import static com.personal.groucho.game.controller.Orientation.RIGHT;
import static com.personal.groucho.game.controller.Orientation.UP;
import static com.personal.groucho.game.gameobjects.ComponentType.AI;

import static java.lang.Math.abs;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.AI.Action;
import com.personal.groucho.game.AI.actions.AttackActions;
import com.personal.groucho.game.AI.actions.EngageActions;
import com.personal.groucho.game.AI.FSM;
import com.personal.groucho.game.AI.actions.IdleActions;
import com.personal.groucho.game.AI.actions.InvestigateActions;
import com.personal.groucho.game.AI.actions.PatrolActions;
import com.personal.groucho.game.AI.pathfinding.AStar;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.AI.states.Attack;
import com.personal.groucho.game.AI.states.Engage;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.AI.states.Investigate;
import com.personal.groucho.game.AI.states.Patrol;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Utils;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.AI.Sight;

import java.util.ArrayList;
import java.util.List;

public class AIComponent extends WalkingComponent {
    public StateName originalState;
    public FSM fsm;
    public Sight sight = null;
    public GameWorld gameWorld;
    public AStar aStar;
    public Orientation originalOrientation;
    public Node originalPosOnGrid, posOnGrid, playerPosOnGrid, currentNode;
    public List<Node> currentPath;
    public boolean isNodeReached = true;
    public boolean isPlayerEngaged = false;
    public boolean isPlayerReached = false;
    public float elapsedTime;

    public IdleActions idleActions;
    public PatrolActions patrolActions;
    public EngageActions engageActions;
    public AttackActions attackActions;
    public InvestigateActions investigateActions;

    public Idle idle;
    public Patrol patrol;
    public Engage engage;
    public Investigate investigate;
    public Attack attack;

    public AIComponent() {
        idleActions = new IdleActions(this);
        patrolActions = new PatrolActions(this);
        engageActions = new EngageActions(this);
        investigateActions = new InvestigateActions(this);

        idle = new Idle(this);
        patrol = new Patrol(this);
        engage = new Engage(this);
        investigate = new Investigate(this);
        attack = new Attack(this);

        aStar = new AStar();
        fsm = new FSM();
        currentPath = new ArrayList<>();
    }

    public void init(GameWorld gameWorld, StateName currentState){
        this.gameWorld = gameWorld;
        isNodeReached = true;
        isPlayerEngaged = false;
        isPlayerReached = false;
        investigateActions.isInvestigate = false;

        attackActions = new AttackActions(this); // TODO: Use init?

        originalState = currentState;

        switch (originalState) {
            case PATROL:
                fsm.setCurrentState(patrol);
                break;
            case ENGAGE:
                fsm.setCurrentState(engage);
                break;
            case ATTACK:
                fsm.setCurrentState(attack);
                break;
            default:
                fsm.setCurrentState(idle);
                break;
        }

        currentPath.clear();
    }

    @Override
    public void reset() {
        super.reset();
        this.sight = null;
    }

    @Override
    public ComponentType type() { return AI; }

    public void update(float elapsedTime, GameWorld gameWorld) {
        init(gameWorld);
        this.elapsedTime = elapsedTime;
        List<Action> actions = fsm.getActions();
        for (Action action : actions) {
            action.doIt();
        }

        sight.updateSightPosition(posComp.posX, posComp.posY);
        sight.see(gameWorld);
    }

    private void init(GameWorld gameWorld) {
        initComponents();

        if (sight == null) {
            sight = new Sight(this,
                    gameWorld.physics.world,
                    new Vec2(posComp.posX, posComp.posY),
                    posComp.orientation);

            originalPosOnGrid = GameGrid.getInstance().getNode(posComp.getPosXOnGrid(), posComp.getPosYOnGrid());
            originalOrientation = posComp.orientation;
            spriteComp.setAnim(posComp.orientation.getValue());
        }
    }

    public Sight getSight() {return sight;}
    public boolean isPlayerVisible() {return gameWorld.player.isPlayerVisible;}
    public void setPlayerEngaged(boolean isPlayerEngaged) {this.isPlayerEngaged = isPlayerEngaged;}

    public void setPathToPlayer() {
        posOnGrid = GameGrid.getInstance().getNode(posComp.getPosXOnGrid(), posComp.getPosYOnGrid());

        playerPosOnGrid = GameGrid.getInstance().getNode(
                gameWorld.player.posX /cellSize,
                gameWorld.player.posY /cellSize
        );

        currentPath.clear();
        currentPath = aStar.findPath(posOnGrid, playerPosOnGrid);
    }

    public void walkingToDestination() {
        if (isNodeReached || currentNode == null) {
            currentNode = currentPath.remove(0);
            isNodeReached = false;
        }

        posOnGrid = GameGrid.getInstance().getNode(posComp.getPosXOnGrid(), posComp.getPosYOnGrid());

        if (posComp.posX != (currentNode.posX*cellSize)+0.5*cellSize) {
            walkingToXCoordinate(posComp.posX, (int)((currentNode.posX*cellSize)+0.5*cellSize));
        }
        else if (posComp.posY != (currentNode.posY*cellSize)+0.5*cellSize) {
            walkingToYCoordinate(posComp.posY, (int)((currentNode.posY*cellSize)+0.5*cellSize));
        }

        if (posOnGrid.equal(currentNode)) {
            isNodeReached = true;
            currentNode = null;
        }
    }

    private void walkingToXCoordinate(int startX, int targetPosX){
        if (abs(startX - targetPosX) < abs(Utils.toBufferXLength(phyIncreaseX))) {
            phyComp.setPosX(targetPosX);
            return;
        }

        if (startX < targetPosX) {
            posComp.setOrientation(RIGHT);
        }
        if (startX > targetPosX) {
            posComp.setOrientation(LEFT);
        }
        walking(elapsedTime);
    }

    private void walkingToYCoordinate(int startY, int targetPosY){
        if (abs(startY - targetPosY) < abs(charComp.properties.speed* phyIncreaseY)) {
            phyComp.setPosY(targetPosY);
            return;
        }

        if (startY < targetPosY) {
            posComp.setOrientation(DOWN);
        }
        if (startY > targetPosY) {
            posComp.setOrientation(UP);
        }
        walking(elapsedTime);
    }

    @Override
    public void walking(float elapsedTime) {
        super.walking(elapsedTime);
        sight.updateSightPosition(posComp.posX, posComp.posY);
        sight.setNewOrientation(posComp.orientation);
    }

    public void updateDirection(Orientation orientation){
        posComp.setOrientation(orientation);
        spriteComp.setAnim(posComp.orientation.getValue());
        sight.setNewOrientation(posComp.orientation);
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
