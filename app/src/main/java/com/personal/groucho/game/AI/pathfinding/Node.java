package com.personal.groucho.game.AI.pathfinding;

import com.personal.groucho.game.gameobjects.Resettable;

public class Node implements Resettable {
    public int posX, posY;
    protected int costToNode, heuristicCostToGoal, defaultCost;
    public Node parent;

    public Node(){
        this.posX = -1;
        this.posY = -1;
        this.parent = null;
        this.costToNode = 0;
        this.heuristicCostToGoal = 0;
        this.defaultCost = 0;
    }

    public Node(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.parent = null;
        this.costToNode = 0;
        this.heuristicCostToGoal = 0;
        this.defaultCost = 0;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getCostToNode() {return costToNode + defaultCost;}
    public int getTotalCost() {return heuristicCostToGoal + costToNode + defaultCost;}

    public void setPosX(int posX) {this.posX = posX;}
    public void setPosY(int posY) {this.posY = posY;}
    public void setCostToNode(int gCost) {this.costToNode = gCost;}
    public void setHeuristicCostToGoal(int hCost) {this.heuristicCostToGoal = hCost;}

    public boolean equal(Node other){
        return posX == other.posX && posY == other.posY;
    }

    public void clear() {
        costToNode = 0;
        heuristicCostToGoal = 0;
        parent = null;
    }

    public void reset(){
        posX = -1;
        posY = -1;
        defaultCost = 0;
        costToNode = 0;
        heuristicCostToGoal = 0;
        parent = null;
    }
}
