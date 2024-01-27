package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;

import android.util.Log;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.collisions.MyContactListener;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.components.Component;
import com.personal.groucho.game.gameobjects.components.ComponentType;
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
            Component phyComponent = go.getComponent(ComponentType.Physics);
            Component posComponent = go.getComponent(ComponentType.Position);
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
            if (event.GO1.role == Role.FURNITURE || event.GO2.role == Role.FURNITURE) {
                Log.d("GW", "Collision with furniture...");
                // Furniture collision sound
            }
        }
    }

    public World getWorld() {return world;}
}
