package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.assets.Sounds.bulletHitEnemy;
import static com.personal.groucho.game.assets.Sounds.bulletHitFurniture;
import static com.personal.groucho.game.constants.CharacterProperties.grouchoPower;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;

public class Events {
    public static void hitEnemyEvent(GameObject hitGO) {
        bulletHitEnemy.play(1f);
        AliveComponent alive = (AliveComponent) hitGO.getComponent(ComponentType.ALIVE);
        if (alive.getCurrentStatus() != DEAD) alive.damage(grouchoPower);
    }

    public static void hitFurnitureEvent( GameObject hitGO, float originX, float originY) {
        bulletHitFurniture.play(1f);
        PhysicsComponent physics = (PhysicsComponent) hitGO.getComponent(ComponentType.PHYSICS);
        float goPosX = physics.getPositionX();
        float goPosY = physics.getPositionY();
        float forceX = goPosX - fromBufferToMetersX(originX);
        float forceY = goPosY - fromBufferToMetersY(originY);
        float module = (float) Math.sqrt(Math.pow(forceX,2) + Math.pow(forceY, 2));

        Vec2 force = new Vec2(20*(forceX/module), 20*(forceY/module));

        physics.applyForce(force);
    }

    public static void hitWallEvent() {
        bulletHitFurniture.play(1f);
    }
}
