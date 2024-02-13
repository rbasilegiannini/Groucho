package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.distanceBetweenPosition;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.isInCircle;
import static com.personal.groucho.game.assets.Sounds.bodyHitFurniture;
import static com.personal.groucho.game.assets.Sounds.bulletHitEnemy;
import static com.personal.groucho.game.assets.Sounds.bulletHitFurniture;
import static com.personal.groucho.game.assets.Sounds.click;
import static com.personal.groucho.game.assets.Sounds.healing;
import static com.personal.groucho.game.assets.Sounds.stabbing;
import static com.personal.groucho.game.constants.CharacterProperties.grouchoPower;
import static com.personal.groucho.game.constants.CharacterProperties.hearingRangeSqr;
import static com.personal.groucho.game.constants.CharacterProperties.medicalKit;
import static com.personal.groucho.game.constants.Environment.brightness;
import static com.personal.groucho.game.constants.Environment.minBrightness;
import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Role.ENEMY;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Events {
    public static void playerShootEnemyEvent(GameObject enemy) {
        bulletHitEnemy.play(1f);
        AliveComponent alive = (AliveComponent) enemy.getComponent(ComponentType.ALIVE);
        if (alive.getCurrentStatus() != DEAD) alive.damage(grouchoPower);
    }

    public static void playerShootFurnitureEvent(GameObject furniture, float originX, float originY) {
        bulletHitFurniture.play(1f);
        PhysicsComponent physics = (PhysicsComponent) furniture.getComponent(ComponentType.PHYSICS);
        float goPosX = physics.getPositionX();
        float goPosY = physics.getPositionY();
        float forceX = goPosX - fromBufferToMetersX(originX);
        float forceY = goPosY - fromBufferToMetersY(originY);
        float module = (float) Math.sqrt(Math.pow(forceX,2) + Math.pow(forceY, 2));

        Vec2 force = new Vec2(20*(forceX/module), 20*(forceY/module));

        physics.applyForce(force);
    }

    public static void playerShootWallEvent() {
        bulletHitFurniture.play(1f);
    }

    public static void playerCollideWithFurniture(GameObject player, GameWorld gameWorld) {
        bodyHitFurniture.play(0.7f);
        Vec2 playerPosition = ((PositionComponent) player.getComponent(POSITION)).getPosition();

        List<PositionComponent> enemiesPosition = new ArrayList<>();
        List<Float> enemiesDistance = new ArrayList<>();
        for (GameObject enemy : gameWorld.getGOByRole(ENEMY)){
            PositionComponent enemyPosition = (PositionComponent) enemy.getComponent(POSITION);
            if (isInCircle(
                    enemyPosition.getPosX(), playerPosition.getX(),
                    enemyPosition.getPosY(), playerPosition.getY(), hearingRangeSqr)){

                float distance = distanceBetweenPosition(
                        playerPosition,
                        new Vec2(enemyPosition.getPosX(), enemyPosition.getPosY()));
                enemiesPosition.add(enemyPosition);
                enemiesDistance.add(distance);
            }
        }
        if (!enemiesDistance.isEmpty()) {
            int indexLessFraction = enemiesDistance.indexOf(Collections.min(enemiesDistance));
            AIComponent aiEnemy = (AIComponent) enemiesPosition.get(indexLessFraction).getOwner().getComponent(AI);
            if (!aiEnemy.isPlayerEngaged()) {
                aiEnemy.setInvestigateStatus(true);
            }
        }
    }

    public static void playerCollideWithHealth(GameObject player, GameObject health) {
        healing.play(0.7f);
        AliveComponent alive = (AliveComponent) player.getComponent(ComponentType.ALIVE);
        alive.heal(medicalKit);

        health.delete();
    }

    public static void enemyHitPlayerEvent(AliveComponent alive, int power) {
        stabbing.play(1f);
        alive.damage(power);
    }

    public static void turnOnLightEvent(GameWorld gameWorld){
        click.play(1f);
        if (brightness == minBrightness) {
            gameWorld.setPlayerVisibility(true);
        }
    }

    public static void turnOffLightEvent(GameWorld gameWorld){
        click.play(1f);
        if (brightness == minBrightness) {
            gameWorld.setPlayerVisibility(false);
        }
    }

    public static void gameOverEvent(GameWorld gameWorld) {
        brightness = 1f;

        gameWorld.getPlayerGO().removeComponent(ComponentType.ALIVE);
        gameWorld.getPlayerGO().removeComponent(ComponentType.CONTROLLABLE);
        gameWorld.getPlayerGO().removeComponent(ComponentType.PHYSICS);
        gameWorld.getPlayerGO().removeComponent(ComponentType.LIGHT);

        gameWorld.GameOver();
    }
}
