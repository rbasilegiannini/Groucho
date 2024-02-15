package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.directionBetweenGO;
import static com.personal.groucho.game.Utils.distBetweenVec;
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
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
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
        float goPosX = physics.getPosX();
        float goPosY = physics.getPosY();
        float forceX = goPosX - fromBufferToMetersX(originX);
        float forceY = goPosY - fromBufferToMetersY(originY);
        float module = (float) Math.sqrt(Math.pow(forceX,2) + Math.pow(forceY, 2));

        Vec2 force = new Vec2(20*(forceX/module), 20*(forceY/module));

        physics.applyForce(force);
    }

    public static void playerShootWallEvent() {
        bulletHitFurniture.play(1f);
    }

    public static void playerCollideWithFurnitureEvent(GameObject player, GameWorld gameWorld) {
        bodyHitFurniture.play(0.7f);
        Vec2 playerPos = ((PositionComponent) player.getComponent(POSITION)).getPos();
        alertEnemiesEvent(gameWorld, playerPos);
    }

    public static void alertEnemiesEvent(GameWorld gameWorld, Vec2 playerPos) {
        List<PositionComponent> enemiesPos = new ArrayList<>();
        List<Float> enemiesDist = new ArrayList<>();
        for (GameObject enemy : gameWorld.getGOByRole(ENEMY)){
            if (((AliveComponent) enemy.getComponent(ALIVE)).getCurrentStatus() != DEAD) {
                PositionComponent enemyPos = (PositionComponent) enemy.getComponent(POSITION);
                if (isInCircle(
                        playerPos.getX(), playerPos.getY(), enemyPos.getPosX(), enemyPos.getPosY(),
                        hearingRangeSqr)) {

                    float dist = distBetweenVec(playerPos, new Vec2(enemyPos.getPosX(), enemyPos.getPosY()));
                    enemiesPos.add(enemyPos);
                    enemiesDist.add(dist);
                }
            }
        }
        if (!enemiesDist.isEmpty()) {
            int indexLessFraction = enemiesDist.indexOf(Collections.min(enemiesDist));
            AIComponent aiEnemy = (AIComponent) enemiesPos.get(indexLessFraction).getOwner().getComponent(AI);
            if (!aiEnemy.isPlayerEngaged()) {
                aiEnemy.setInvestigateStatus(true);
            }
        }
    }

    public static void playerCollideWithHealthEvent(GameObject player, GameObject health) {
        healing.play(0.7f);
        AliveComponent alive = (AliveComponent) player.getComponent(ComponentType.ALIVE);
        alive.heal(medicalKit);

        health.delete();
    }

    public static void playerCollideWithEnemyEvent(GameWorld gameWorld, GameObject enemy) {
        AIComponent aiEnemy = (AIComponent) enemy.getComponent(AI);
        if (!aiEnemy.isPlayerEngaged()) {
            aiEnemy.updateDirection(directionBetweenGO(gameWorld.getPlayerGO(), enemy));
            aiEnemy.setPlayerEngaged(true);
        }
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
        gameWorld.GameOver();
    }
}
