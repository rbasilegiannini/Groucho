package com.personal.groucho.game.AI.actions;

import static com.personal.groucho.game.constants.System.maxInvestigateTime;

import com.personal.groucho.game.AI.Actions;
import com.personal.groucho.game.gameobjects.components.AIComponent;

public class InvestigateActions implements Actions {
    private final AIComponent aiComp;
    public boolean isInvestigate = false;
    private long investigateTimeMillis;

    public InvestigateActions(AIComponent aiComp) {
        this.aiComp = aiComp;
    }

    @Override
    public void entryAction() {
        aiComp.setPathToPlayer();
        aiComp.isNodeReached = true;
        isInvestigate = true;
        investigateTimeMillis = System.currentTimeMillis();
        aiComp.charComp.properties.call.play(1f);
    }

    @Override
    public void activeAction() {
        if (System.currentTimeMillis() - investigateTimeMillis > maxInvestigateTime) {
            isInvestigate = false;
            return;
        }

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
    public void updatePath() {aiComp.setPathToPlayer();}
}
