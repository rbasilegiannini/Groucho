package com.personal.groucho.game.AI.actions;

import static com.personal.groucho.game.Utils.isCloseToGO;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.System.maxInvisiblePlayer;

import com.personal.groucho.game.AI.Actions;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.components.AIComponent;

public class EngageActions implements Actions {
    private final AIComponent aiComp;
    public long lastSeenMills;

    public EngageActions(AIComponent aiComp) {
        this.aiComp = aiComp;
    }

    @Override
    public void entryAction() {
        aiComp.initComponents();

        aiComp.posOnGrid = GameGrid.getInstance().getNode(
                aiComp.posComp.getPosXOnGrid(),
                aiComp.posComp.getPosYOnGrid()
        );

        lastSeenMills = System.currentTimeMillis();
        aiComp.setPathToPlayer();
        aiComp.isNodeReached = true;
        aiComp.charComp.properties.call.play(1f);
    }

    @Override
    public void activeAction() {
        if (hasPlayerChangedPosition() && aiComp.currentNode == null) {
            aiComp.setPathToPlayer();
        }
        aiComp.isPlayerReached = isCloseToGO(
                aiComp.posComp.posX, aiComp.posComp.posY, aiComp.gameWorld.player.gameObject);

        if (!aiComp.currentPath.isEmpty() || !aiComp.isNodeReached) {
            aiComp.walkingToDestination();
        }
        else {
            aiComp.isPlayerReached = false;
        }

        if (!aiComp.gameWorld.player.isPlayerVisible) {
            if (System.currentTimeMillis() - lastSeenMills > maxInvisiblePlayer){
                aiComp.isPlayerEngaged = false;
            }
        }
        else {
            lastSeenMills = System.currentTimeMillis();
        }
    }

    @Override
    public void exitAction() {
        aiComp.currentPath.clear();
        aiComp.currentPath = aiComp.aStar.findPath(aiComp.posOnGrid, aiComp.originalPosOnGrid);
    }

    private boolean hasPlayerChangedPosition() {
        return !aiComp.playerPosOnGrid.equal(GameGrid.getInstance().getNode(
                aiComp.gameWorld.player.posX / cellSize,
                aiComp.gameWorld.player.posY / cellSize));
    }
}
