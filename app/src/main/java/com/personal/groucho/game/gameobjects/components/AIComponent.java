package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.cellSize;
import static com.personal.groucho.game.Constants.skeletonSpeed;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_hurt;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_idle;
import static com.personal.groucho.game.assets.Spritesheets.skeleton_walk;

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
    private Sight sight = null;
    private final GameWorld gameWorld;
    private final AStar aStar;
    private Node positionOnGrid;
    private Node playerPositionOnGrid;
    private List<Node> currentPath;
    private Node current;
    private boolean newNode = true;

    public AIComponent(GameWorld gameWorld, GameGrid grid) {
        this.gameWorld = gameWorld;
        fsm = new FSM(new Idle(this));
        aStar = new AStar(grid);
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
                    gameWorld,
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
    }

    public void entryEngageAction(){
        if (positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        positionOnGrid = gameWorld.getGameGridNode(
                positionComponent.getPositionXOnGrid(),
                positionComponent.getPositionYOnGrid()
        );

        setPathToPlayer();
    }

    public void activeEngageAction(){
        if (!playerPositionOnGrid.equal(gameWorld.getGameGridNode(
                (int) gameWorld.getPlayerPosition().getX()/cellSize,
                (int) gameWorld.getPlayerPosition().getY()/cellSize
        )))
            setPathToPlayer();

        gameWorld.setPlayerReached(gameWorld.isAPlayerNeighbor(positionOnGrid));

        if (!currentPath.isEmpty() || !newNode) {
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
        else {
            updateSprite(skeleton_idle);
            // Come back to patrol. And exit action will be returning to original position
        }
    }

    public void entryAttackAction() {
        sight.setNewOrientation(positionComponent.getOrientation());
        updateSprite(skeleton_hurt);
    }

    public void activeAttackAction() {
        gameWorld.setPlayerReached(gameWorld.isAPlayerNeighbor(positionOnGrid));
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
            walking(skeleton_walk, skeletonSpeed);
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
            walking(skeleton_walk, skeletonSpeed);
        }
    }

    @Override
    protected void walking(Spritesheet sheet, float speed) {
        super.walking(sheet, speed);
        sight.updateSight(positionComponent.getPosition());
        sight.setNewOrientation(positionComponent.getOrientation());
    }

}
