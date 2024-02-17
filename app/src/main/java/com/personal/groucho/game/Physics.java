package com.personal.groucho.game;

import static com.personal.groucho.game.Events.playerCollideWithEnemyEvent;
import static com.personal.groucho.game.Events.playerCollideWithFurnitureEvent;
import static com.personal.groucho.game.Events.playerCollideWithHealthEvent;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Role.ENEMY;
import static com.personal.groucho.game.gameobjects.Role.FURNITURE;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.collisions.MyContactListener;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Physics {
    private final GameWorld gameWorld;
    private GameGrid gameGrid;
    protected final World world;
    private final MyContactListener contactListener;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    // To avoid further allocations
    private GameObject currentGO = null;
    private final List<GameObject> hitGameObjects = new ArrayList<>();
    private final List<Float> fractions = new ArrayList<>();
    private Set<Node> oldCellsToReset, newCellsToChange, unchangedCells;


    public Physics(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.world = new World(0, 0); // No gravity
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);
    }

    public void setGameGrid(GameGrid grid) {this.gameGrid = grid;}

    public synchronized void update(float elapsedTime) {
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        updatePhysicsPosition();
        handleCollisions();
    }

    private void updatePhysicsPosition() {
        for (PhysicsComponent phyComponent : gameWorld.phyComponents) {
            currentGO = (GameObject)(phyComponent.getOwner());
            PositionComponent posComponent = (PositionComponent) currentGO.getComponent(POSITION);

            if (currentGO.role == FURNITURE) {
                handleFurnitureCollision(phyComponent, posComponent);
            }
            else {
                if (phyComponent.hasChangedPosition()) {
                    posComponent.setPosX((int) fromMetersToBufferX(phyComponent.getPosX()));
                    posComponent.setPosY((int) fromMetersToBufferY(phyComponent.getPosY()));
                }
            }
        }
    }

    private void handleFurnitureCollision(PhysicsComponent phyComponent, PositionComponent posComponent) {
        float originalPosX = fromMetersToBufferX(phyComponent.originalPosX);
        float originalPosY = fromMetersToBufferY(phyComponent.originalPosY);

        if (phyComponent.hasChangedPosition()) {
            posComponent.setPosX((int) fromMetersToBufferX(phyComponent.getPosX()));
            posComponent.setPosY((int) fromMetersToBufferY(phyComponent.getPosY()));

            updateGameGrid(phyComponent, originalPosX, originalPosY);
        }
    }

    private void updateGameGrid(PhysicsComponent phyComponent, float originalPosX, float originalPosY) {
        if (gameGrid != null) {
            int dCost = (int)(phyComponent.density*10000);

            oldCellsToReset = gameGrid.getNodes(
                    (int)originalPosX,
                    (int)originalPosY,
                    (int)phyComponent.dimX,
                    (int)phyComponent.dimY
            );
            newCellsToChange = gameGrid.getNodes(
                    (int)fromMetersToBufferX(phyComponent.getPosX()),
                    (int)fromMetersToBufferY(phyComponent.getPosY()),
                    (int)phyComponent.dimX,
                    (int)phyComponent.dimY
            );

            unchangedCells = new HashSet<>(newCellsToChange);
            unchangedCells.retainAll(oldCellsToReset);

            newCellsToChange.removeAll(unchangedCells);
            oldCellsToReset.removeAll(unchangedCells);

            // Update cost
            for (Node node : newCellsToChange) {
                gameGrid.increaseDefaultCostOnNode(node, dCost);
            }

            // Reset cost
            for (Node node : oldCellsToReset) {
                gameGrid.decreaseDefaultCostOnNode(node, dCost);
            }
        }
    }

    public GameObject reportGameObject(float originX, float originY, float endX, float endY) {
        hitGameObjects.clear();
        fractions.clear();

        currentGO = null;

        world.rayCast(
                new RayCastCallback() {
                    public float reportFixture(Fixture fixture, Vec2 i, Vec2 n, float fraction) {
                        GameObject hitGO = (GameObject) fixture.getBody().getUserData();
                        hitGameObjects.add(hitGO);
                        fractions.add(fraction);

                        return fraction;
                    }
                },
                fromBufferToMetersX(originX),
                fromBufferToMetersY(originY),
                fromBufferToMetersX(endX),
                fromBufferToMetersY(endY)
        );
        if (!fractions.isEmpty()) {
            int indexLessFraction = fractions.indexOf(Collections.min(fractions));
            currentGO = hitGameObjects.get(indexLessFraction);
        }

        return currentGO;
    }

    private void handleCollisions() {
        for (Collision event: contactListener.getCollisions()) {
            if (event.GO1.role == PLAYER)
                handlePlayerCollision(event.GO1, event.GO2);
            else if (event.GO2.role == PLAYER)
                handlePlayerCollision(event.GO2, event.GO1);
            else if (event.GO1.role == ENEMY)
                handleEnemyCollision(event.GO1, event.GO2);
            else if (event.GO2.role == ENEMY)
                handleEnemyCollision(event.GO2, event.GO1);
        }
    }

    private void handlePlayerCollision(GameObject player, GameObject object) {
        switch (object.role) {
            case ENEMY:
                playerCollideWithEnemyEvent(gameWorld, object);
                break;

            case FURNITURE:
                playerCollideWithFurnitureEvent(gameWorld);
                break;

            case HEALTH:
                playerCollideWithHealthEvent(player, object);
                break;

            case TRIGGER:
                gameWorld.getLevel().handleTrigger(object);
                break;
        }
    }

    private void handleEnemyCollision(GameObject enemy, GameObject object) {
        if (object.role == PLAYER) {
            playerCollideWithEnemyEvent(gameWorld, enemy);
        }
    }
}
