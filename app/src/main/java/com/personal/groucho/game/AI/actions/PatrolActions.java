package com.personal.groucho.game.AI.actions;

import com.personal.groucho.game.AI.Actions;
import com.personal.groucho.game.gameobjects.components.AIComponent;

public class PatrolActions implements Actions {
    private final AIComponent aiComp;
    private final int maxSteps;
    private int currentSteps = 0;
    private boolean isPatrol = false;

    public PatrolActions(AIComponent aiComp) {
        this.aiComp = aiComp;
        maxSteps = 100;
    }

    @Override
    public void entryAction() {
        currentSteps = 0;
        aiComp.updateSprite(aiComp.character.properties.sheetWalk);
        aiComp.posComp.setOrientation(aiComp.posComp.orientation.getOpposite());
    }

    @Override
    public void activeAction() {
        if (!aiComp.currentPath.isEmpty() || !aiComp.isNodeReached)
            aiComp.walkingToDestination();
        else {
            if (!isPatrol) {
                aiComp.posComp.setOrientation(aiComp.originalOrientation);
                aiComp.sight.setNewOrientation(aiComp.originalOrientation);
                isPatrol = true;
            }
            patrol();
        }
    }

    @Override
    public void exitAction() {
        isPatrol = false;
        currentSteps = 0;
    }

    private void patrol() {
        if (currentSteps == maxSteps) {
            currentSteps = 0;
            aiComp.posComp.setOrientation(aiComp.posComp.orientation.getOpposite());
        }
        currentSteps++;
        aiComp.walking();
    }
}