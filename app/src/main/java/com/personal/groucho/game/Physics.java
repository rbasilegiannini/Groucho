package com.personal.groucho.game;

import static com.personal.groucho.game.Constants.medicalKit;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.assets.Sounds.bodyHitFurniture;
import static com.personal.groucho.game.assets.Sounds.healing;

import android.util.Log;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.collisions.MyContactListener;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Physics {
    private final Box physicalSize;
    private final World world;
    private final MyContactListener contactListener;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    public Physics(Box physicalSize) {
        this.world = new World(0, 0); // No gravity
        this.physicalSize = physicalSize;
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);
    }

    public synchronized void update(float elapsedTime, List<GameObject> objects) {
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        updatePhysicsPosition(objects);
        handleCollisions();
    }

    private void updatePhysicsPosition(List<GameObject> objects) {
        // TODO: use a better solution
        for (GameObject go : objects) {
            Component phyComponent = go.getComponent(ComponentType.PHYSICS);
            Component posComponent = go.getComponent(ComponentType.POSITION);
            if (phyComponent != null && posComponent != null) {
                PhysicsComponent physicsComponent = (PhysicsComponent) phyComponent;
                PositionComponent positionComponent = (PositionComponent) posComponent;
                positionComponent.setPosX(
                        (int) fromMetersToBufferX(physicsComponent.getPositionX())
                );
                positionComponent.setPosY(
                        (int) fromMetersToBufferY(physicsComponent.getPositionY())
                );
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
            case FURNITURE:
                bodyHitFurniture.play(0.7f);
                break;

            case HEALTH:
                healing.play(0.7f);
                AliveComponent alive = (AliveComponent) player.getComponent(ComponentType.ALIVE);
                alive.heal(medicalKit);

                object.delete();
                break;
        }
    }

    private void handleEnemyCollision(GameObject enemy, GameObject object) {
        PositionComponent position = (PositionComponent) enemy.getComponent(ComponentType.POSITION);
        switch (object.role) {
            case WALL:
            case HEALTH:
            case FURNITURE:
                position.setOrientation(position.getOrientation().getTurnOnRight());
                break;
        }
    }

    public World getWorld() {return world;}
}
