package com.personal.groucho.game;

import static com.personal.groucho.game.Events.playerCollideWithFurniture;
import static com.personal.groucho.game.Events.playerCollideWithHealth;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Role.FURNITURE;

import android.util.Log;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.collisions.MyContactListener;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;
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
    private final World world;
    private final MyContactListener contactListener;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

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
            GameObject go = (GameObject)(phyComponent.getOwner());
            PositionComponent posComponent = (PositionComponent) go.getComponent(POSITION);

            if (go.role == FURNITURE) {
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
        Vec2 originalPos = new Vec2(
                fromMetersToBufferX(phyComponent.getOriginalPosX()),
                fromMetersToBufferY(phyComponent.getOriginalPosY())
        );

        if (phyComponent.hasChangedPosition()) {
            posComponent.setPosX((int) fromMetersToBufferX(phyComponent.getPosX()));
            posComponent.setPosY((int) fromMetersToBufferY(phyComponent.getPosY()));

            updateGameGrid(phyComponent, originalPos);
        }
    }

    private void updateGameGrid(PhysicsComponent physics, Vec2 oldPos) {
        if (gameGrid != null) {
            int oldPosX = (int) oldPos.getX();
            int oldPosY = (int) oldPos.getY();
            int posX = (int)fromMetersToBufferX(physics.getPosX());
            int posY = (int)fromMetersToBufferY(physics.getPosY());
            int dimX = (int)physics.getDimX();
            int dimY = (int)physics.getDimY();
            int dCost = (int)(physics.getDensity()*10000);

            Set<Node> oldCellsToReset = gameGrid.getNodes(oldPosX, oldPosY, dimX, dimY);
            Set<Node> newCellsToChange = gameGrid.getNodes(posX, posY, dimX, dimY);

            Set<Node> unchangedCells = new HashSet<>(newCellsToChange);
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
        List<GameObject> hitGameObjects = new ArrayList<>();
        List<Float> fractions = new ArrayList<>();
        GameObject firstGO = null;

        world.rayCast(
                new RayCastCallback() {
                    public float reportFixture(Fixture fixture, Vec2 i, Vec2 n, float fraction) {
                        Log.i("RayCast", "hit");
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
            firstGO = hitGameObjects.get(indexLessFraction);
        }

        return firstGO;
    }

    private void handleCollisions() {
        for (Collision event: contactListener.getCollisions()) {
            if (event.GO1.role == Role.PLAYER)
                handlePlayerCollision(event.GO1, event.GO2);
            else if (event.GO2.role == Role.PLAYER)
                handlePlayerCollision(event.GO2, event.GO1);
            else if (event.GO1.role == Role.ENEMY)
                handleEnemyCollision(event.GO1, event.GO2);
            else if (event.GO2.role == Role.ENEMY)
                handleEnemyCollision(event.GO2, event.GO1);
        }
    }

    private void handlePlayerCollision(GameObject player, GameObject object) {
        switch (object.role) {
            case ENEMY:
                // Enemy alert
                break;

            case FURNITURE:
                playerCollideWithFurniture(player, gameWorld);
                break;

            case HEALTH:
                playerCollideWithHealth(player, object);
                break;

            case TRIGGER:
                gameWorld.getLevel().handleTrigger(object);
                break;
        }
    }

    private void handleEnemyCollision(GameObject enemy, GameObject object) {
        // What happen if enemy collides with player?
    }

    public World getWorld() {return world;}
}
