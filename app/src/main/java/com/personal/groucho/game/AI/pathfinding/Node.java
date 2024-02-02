package com.personal.groucho.game.AI.pathfinding;

public class Node {
    public int posX, posY;
    public int costToNode, heuristicCostToGoal;
    public Node parent;

    public Node(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.parent = null;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getPosX() {return posX;}
    public int getPosY() {return posY;}
    public int getCostToNode() {return costToNode;}
    public int getHeuristicCostToGoal() {return heuristicCostToGoal;}
    public int getTotalCost() {return heuristicCostToGoal + costToNode;}

    public void setPosX(int posX) {this.posX = posX;}
    public void setPosY(int posY) {this.posY = posY;}
    public void setCostToNode(int gCost) {this.costToNode = gCost;}
    public void setHeuristicCostToGoal(int hCost) {this.heuristicCostToGoal = hCost;}

    public boolean equal(Node other){
        return posX == other.getPosX() && posY == other.getPosY();
    }

    public void reset() {
        this.costToNode = 0;
        this.heuristicCostToGoal = 0;
        this.parent = null;
    }
}
