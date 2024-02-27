package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.directionBetweenGO;
import static com.personal.groucho.game.Utils.distBetweenPos;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.isInCircle;
import static com.personal.groucho.game.assets.Sounds.bodyHitFurniture;
import static com.personal.groucho.game.assets.Sounds.bulletHitEnemy;
import static com.personal.groucho.game.assets.Sounds.bulletHitFurniture;
import static com.personal.groucho.game.assets.Sounds.click;
import static com.personal.groucho.game.assets.Sounds.healing;
import static com.personal.groucho.game.assets.Sounds.stabbing;
import static com.personal.groucho.game.constants.Character.grouchoPower;
import static com.personal.groucho.game.constants.Character.hearingRangeSqr;
import static com.personal.groucho.game.constants.Character.medicalKit;
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
        AliveComponent aliveComp = (AliveComponent) enemy.getComponent(ComponentType.ALIVE);
        if (aliveComp.currentStatus != DEAD) aliveComp.damage(grouchoPower);
    }

    public static void playerShootFurnitureEvent(GameObject furniture, float originX, float originY) {
        bulletHitFurniture.play(1f);
        PhysicsComponent phyComp = (PhysicsComponent) furniture.getComponent(ComponentType.PHYSICS);
        float goPosX = phyComp.getPosX();
        float goPosY = phyComp.getPosY();
        float forceX = goPosX - fromBufferToMetersX(originX);
        float forceY = goPosY - fromBufferToMetersY(originY);
        float module = (float) Math.sqrt(Math.pow(forceX,2) + Math.pow(forceY, 2));

        Vec2 force = new Vec2(20*(forceX/module), 20*(forceY/module));

        phyComp.applyForce(force);
    }

    public static void playerShootWallEvent() {
        bulletHitFurniture.play(1f);
    }

    public static void playerCollideWithFurnitureEvent(GameWorld gameWorld) {
        bodyHitFurniture.play(0.7f);
        alertEnemiesEvent(gameWorld);
    }

    public static void alertEnemiesEvent(GameWorld gameWorld) {
        List<PositionComponent> enemiesPos = new ArrayList<>();
        List<Float> enemiesDist = new ArrayList<>();

        for (GameObject enemy : gameWorld.goHandler.getGOByRole(ENEMY)){
            if (enemyIsAlive(enemy)) {
                PositionComponent enemyPos = (PositionComponent) enemy.getComponent(POSITION);
                float playerPosX = gameWorld.player.posX;
                float playerPosY = gameWorld.player.posY;

                if (playerIsAudible(enemyPos, playerPosX, playerPosY)) {
                    float dist = distBetweenPos(playerPosX, playerPosY, enemyPos.posX, enemyPos.posY);
                    enemiesPos.add(enemyPos);
                    enemiesDist.add(dist);
                }
            }
        }
        if (!enemiesDist.isEmpty()) {
            callNearestEnemy(enemiesPos, enemiesDist);
        }
    }

    private static boolean playerIsAudible(PositionComponent enemyPos, float playerPosX, float playerPosY) {
        return isInCircle(playerPosX, playerPosY, enemyPos.posX, enemyPos.posY, hearingRangeSqr);
    }

    private static boolean enemyIsAlive(GameObject enemy) {
        return ((AliveComponent) enemy.getComponent(ALIVE)).currentStatus != DEAD;
    }

    private static void callNearestEnemy(List<PositionComponent> enemiesPos, List<Float> enemiesDist) {
        int indexLessFraction = enemiesDist.indexOf(Collections.min(enemiesDist));
        AIComponent aiEnemy = (AIComponent) enemiesPos.get(indexLessFraction).getOwner().getComponent(AI);
        if (!aiEnemy.isPlayerEngaged) {
            aiEnemy.investigateActions.setInvestigateStatus(true);
        }
    }

    public static void playerCollideWithHealthEvent(GameObject player, GameObject health, GameWorld gameWorld) {
        healing.play(0.7f);
        AliveComponent aliveComp = (AliveComponent) player.getComponent(ComponentType.ALIVE);
        aliveComp.heal(medicalKit);

        gameWorld.goHandler.removeGameObject(health);
    }

    public static void playerCollideWithEnemyEvent(GameWorld gameWorld, GameObject enemy) {
        AIComponent aiEnemy = (AIComponent) enemy.getComponent(AI);
        if (!aiEnemy.isPlayerEngaged) {
            aiEnemy.updateDirection(directionBetweenGO(gameWorld.player.gameObject, enemy));
            aiEnemy.setPlayerEngaged(true);
        }
    }

    public static void enemyHitPlayerEvent(AliveComponent aliveComp, int power) {
        stabbing.play(1f);
        aliveComp.damage(power);
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

    public static void pauseEvent(GameWorld gameWorld) {
        gameWorld.pause();
        MenuHandler.handlePauseMenu(gameWorld);
    }
}
