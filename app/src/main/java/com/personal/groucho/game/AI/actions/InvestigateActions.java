package com.personal.groucho.game.AI.actions;

import com.personal.groucho.game.AI.Actions;
import com.personal.groucho.game.gameobjects.components.AIComponent;

public class InvestigateActions implements Actions {
    private final AIComponent aiComp;
    public boolean isInvestigate = false;

    public InvestigateActions(AIComponent aiComp) {
        this.aiComp = aiComp;
    }

    @Override
    public void entryAction() {
        aiComp.setPathToPlayer();
        aiComp.isNodeReached = true;
        isInvestigate = true;
    }

    @Override
    public void activeAction() {
        if (!aiComp.currentPath.isEmpty() || !aiComp.isNodeReached) {
            aiComp.walkingToDestination();
        }
        else {
            isInvestigate = false;
        }
    }

    @Override
    public void exitAction() {
        isInvestigate = false;
        aiComp.currentPath.clear();
        aiComp.currentPath = aiComp.aStar.findPath(aiComp.posOnGrid, aiComp.originalPosOnGrid);
    }

    public void setInvestigateStatus(boolean isInvestigate) {this.isInvestigate = isInvestigate;}
}
