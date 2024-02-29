package com.personal.groucho.game.AI.actions;

import com.personal.groucho.game.AI.Actions;
import com.personal.groucho.game.gameobjects.components.AIComponent;

public class IdleActions implements Actions {
    private final AIComponent aiComp;
    private boolean isIdle = false;

    public IdleActions(AIComponent aiComp) {
        this.aiComp = aiComp;
    }

    @Override
    public void entryAction() {
        if (!aiComp.currentPath.isEmpty() || !aiComp.isNodeReached) {
            aiComp.updateSprite(aiComp.charComp.properties.sheetWalk);
        }
        else {
            aiComp.updateSprite(aiComp.charComp.properties.sheetIdle);
        }
    }

    @Override
    public void activeAction() {
        if (!aiComp.currentPath.isEmpty() || !aiComp.isNodeReached)
            aiComp.walkingToDestination();
        else if (!isIdle){
            aiComp.posComp.setOrientation(aiComp.originalOrientation);
            aiComp.sight.setNewOrientation(aiComp.originalOrientation);
            aiComp.updateSprite(aiComp.charComp.properties.sheetIdle);
            isIdle = true;
        }
    }

    @Override
    public void exitAction() {
        isIdle = false;
    }
}
