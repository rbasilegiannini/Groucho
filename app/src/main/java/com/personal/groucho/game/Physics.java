package com.personal.groucho.game;

import static com.personal.groucho.game.Events.playerCollideWithEnemyEvent;
import static com.personal.groucho.game.Events.playerCollideWithFurnitureEvent;
import static com.personal.groucho.game.Events.playerCollideWithHealthEvent;
import static com.personal.groucho.game.Events.playerCollideWithTrigger;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Role.ENEMY;
import static com.personal.groucho.game.gameobjects.Role.FURNITURE;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;

import android.util.SparseArray;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.collisions.MyContactListener;
import com.personal.groucho.game.gameobjects.Entity;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Physics {
    private static Physics instance = null;
    private final GameWorld gameWorld;
    public final World world;
    private final MyContactListener contactListener;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    // To avoid further allocations
    private final List<GameObject> hitGameObjects = new ArrayList<>();
    private final List<Float> fractions = new ArrayList<>();
    private final SparseArray<Node> unchangedCells = new SparseArray<>();

    private Physics(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.world = new World(0, 0); // No gravity
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);
    }

    public static Physics getInstance(GameWorld gameWorld){
        if (instance == null) {
            instance = new Physics(gameWorld);
        }
        return instance;
    }

    public synchronized void update(float elapsedTime) {
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        updatePhysicsPos();
        handleCollisions();
    }

    private void updatePhysicsPos() {
        for (PhysicsComponent phyComp : ComponentHandler.getInstance().phyComps) {
            Entity currentEntity = phyComp.getOwner();
            PositionComponent posComp = (PositionComponent) currentEntity.getComponent(POSITION);

            if (currentEntity.role == FURNITURE) {
                handleFurnitureCollision(phyComp, posComp);
            }
            else {
                if (phyComp.hasChangedPosition()) {
                    posComp.setPosX((int) fromMetersToBufferX(phyComp.getPosX()));
                    posComp.setPosY((int) fromMetersToBufferY(phyComp.getPosY()));
                }
            }
        }
    }

    private void handleFurnitureCollision(PhysicsComponent phyComp, PositionComponent posComp) {
        float originalPosX = fromMetersToBufferX(phyComp.originalPosX);
        float originalPosY = fromMetersToBufferY(phyComp.originalPosY);

        if (phyComp.hasChangedPosition()) {
            posComp.setPosX((int) fromMetersToBufferX(phyComp.getPosX()));
            posComp.setPosY((int) fromMetersToBufferY(phyComp.getPosY()));

            updateGameGrid(phyComp, originalPosX, originalPosY);
        }
    }

    private void updateGameGrid(PhysicsComponent phyComp, float originalPosX, float originalPosY) {
        if (GameGrid.getInstance() != null) {
            int dCost = (int) (phyComp.density * 10000);

            SparseArray<Node> oldCellsToReset = GameGrid.getInstance().getNodes(
                    (int)originalPosX,
                    (int)originalPosY,
                    (int)phyComp.dimX,
                    (int)phyComp.dimY
            ).clone();

            SparseArray<Node> newCellsToChange = GameGrid.getInstance().getNodes(
                    (int)fromMetersToBufferX(phyComp.getPosX()),
                    (int)fromMetersToBufferY(phyComp.getPosY()),
                    (int)phyComp.dimX,
                    (int)phyComp.dimY
            ).clone();

            setUnchangedCells(oldCellsToReset, newCellsToChange);

            for (int i = 0; i < unchangedCells.size(); i++) {
                int key = unchangedCells.keyAt(i);
                newCellsToChange.remove(key);
                oldCellsToReset.remove(key);
            }

            // Update cost
            for (int i = 0; i < newCellsToChange.size(); i++) {
                GameGrid.getInstance().increaseDefaultCostOnNode(newCellsToChange.valueAt(i), dCost);
            }

            // Reset cost
            for (int i = 0; i < oldCellsToReset.size(); i++) {
                GameGrid.getInstance().decreaseDefaultCostOnNode(oldCellsToReset.valueAt(i), dCost);
            }
        }
    }

    private void setUnchangedCells(SparseArray<Node> oldCellsToReset, SparseArray<Node> newCellsToChange) {
        unchangedCells.clear();
        for (int i = 0; i < newCellsToChange.size(); i++) {
            int key = newCellsToChange.keyAt(i);
            if (oldCellsToReset.indexOfKey(key) >= 0) {
                unchangedCells.put(key, newCellsToChange.valueAt(i));
            }
        }
    }

    public GameObject reportGameObject(float originX, float originY, float endX, float endY) {
        hitGameObjects.clear();
        fractions.clear();

        GameObject currentGO = null;

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
        SparseArray<Collision> collisions = contactListener.getCollisions();
        for (int i = 0; i < collisions.size(); i ++) {
            Collision event = collisions.valueAt(i);

            if (event.GO1.role == PLAYER)
                handlePlayerCollision(event.GO1, event.GO2);
            else if (event.GO2.role == PLAYER)
                handlePlayerCollision(event.GO2, event.GO1);
            else if (event.GO1.role == ENEMY)
                handleEnemyCollision(event.GO1, event.GO2);
            else if (event.GO2.role == ENEMY)
                handleEnemyCollision(event.GO2, event.GO1);

            Pools.collisionsPool.release(event);
        }
    }

    private void handlePlayerCollision(GameObject player, GameObject object) {
        switch (object.role) {
            case ENEMY:
                playerCollideWithEnemyEvent(player, object);
                break;

            case FURNITURE:
                playerCollideWithFurnitureEvent(gameWorld);
                break;

            case HEALTH:
                playerCollideWithHealthEvent(object, gameWorld);
                break;

            case TRIGGER:
                playerCollideWithTrigger(object);
                break;
        }
    }

    private void handleEnemyCollision(GameObject enemy, GameObject object) {
        if (object.role == PLAYER) {
            playerCollideWithEnemyEvent(gameWorld.player.gameObject, enemy);
        }
    }

    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        world.delete();
        instance = null;
    }
}
