package com.personal.groucho.game.AI.pathfinding;

public class Node {
    public int posX, posY;
    protected int costToNode, heuristicCostToGoal, defaultCost;
    public Node parent;

    public Node(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.parent = null;
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

    public void reset() {
        this.costToNode = 0;
        this.heuristicCostToGoal = 0;
        this.parent = null;
    }
}
