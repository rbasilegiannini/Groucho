package com.personal.groucho.game.AI.actions;

import static com.personal.groucho.game.Events.enemyHitPlayerEvent;
import static com.personal.groucho.game.Utils.directionBetweenGO;
import static com.personal.groucho.game.Utils.isCloseToGO;
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import com.personal.groucho.game.AI.Actions;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.states.Idle;
import com.personal.groucho.game.AI.states.Patrol;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;

public class AttackActions implements Actions {
    private final AIComponent aiComp;
    private final AliveComponent playerAliveComp;
    private long lastHitMillis;


    public AttackActions(AIComponent aiComp) {
        this.aiComp = aiComp;
        playerAliveComp = (AliveComponent) aiComp.gameWorld.player.gameObject.getComponent(ALIVE);
    }

    @Override
    public void entryAction() {
        lastHitMillis = System.currentTimeMillis();
        aiComp.sight.setNewOrientation(aiComp.posComp.orientation);
        aiComp.updateSprite(aiComp.character.properties.sheetHurt);
    }

    @Override
    public void activeAction() {
        aiComp.initComponents();
        aiComp.isPlayerReached = isCloseToGO(
                aiComp.posComp.posX, aiComp.posComp.posY, aiComp.gameWorld.player.gameObject);

        if (playerIsAlive()) {
            long delay = getDelayHitSpriteMillis();
            if (aiComp.isPlayerReached && System.currentTimeMillis() - lastHitMillis > delay) {
                hitPlayer();
            }
        }
        else {
            comeBack();
        }
    }

    private long getDelayHitSpriteMillis() {
        return aiComp.character.properties.sheetHurt.getDelay(0) *
                        aiComp.character.properties.sheetHurt.getLength(0);
    }

    private void comeBack() {
        aiComp.isPlayerEngaged = false;
        aiComp.isPlayerReached = false;
        aiComp.posOnGrid = GameGrid.getInstance(aiComp.gameWorld).getNode(
                aiComp.posComp.getPosXOnGrid(),
                aiComp.posComp.getPosYOnGrid()
        );
        aiComp.currentPath = aiComp.aStar.findPath(aiComp.posOnGrid, aiComp.originalPosOnGrid);

        switch (aiComp.originalState) {
            case IDLE:
                aiComp.fsm.setState(Idle.getInstance(aiComp));
                break;
            case PATROL:
                aiComp.fsm.setState(Patrol.getInstance(aiComp));
                break;
        }
    }

    private void hitPlayer() {
        aiComp.updateDirection(
                directionBetweenGO(aiComp.gameWorld.player.gameObject, (GameObject) aiComp.getOwner()));
        enemyHitPlayerEvent(playerAliveComp, aiComp.character.properties.power);
        lastHitMillis = System.currentTimeMillis();
    }

    private boolean playerIsAlive() {
        return !aiComp.gameWorld.gameOver && playerAliveComp.currentStatus != DEAD;
    }

    @Override
    public void exitAction() {
        aiComp.isPlayerReached = false;
    }
}
