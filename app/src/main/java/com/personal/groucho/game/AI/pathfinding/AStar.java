package com.personal.groucho.game.AI.pathfinding;

import static com.personal.groucho.game.constants.System.cellSize;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.personal.groucho.game.GameWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    private GameWorld gameWorld;
    private final PriorityQueue<Node> openSet;
    private final SparseArray<Node> closedSet = new SparseArray<>();
    private final List<Node> neighbors = new ArrayList<>();
    private final List<Node> path = new ArrayList<>();

    public AStar() {
        this.openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
    }

    public void init(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public List<Node> findPath(Node start, Node goal) {
        clear();

        openSet.add(start);
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.equal(goal))
                return buildPath(current);

            processNeighbors(current, goal);
        }

        return Collections.emptyList(); // No path found
    }

    @SuppressLint("NewApi")
    private void processNeighbors(Node current, Node goal) {
        closedSet.put(current.hashCode(), current);
        for (Node neighbor : getNeighbors(current)) {
            if (closedSet.indexOfKey(neighbor.hashCode()) >= 0)
                continue; // Ignore already evaluated neighbor

            int tentativeGCost = current.getCostToNode() + manhattan(current, neighbor);

            if (!openSet.contains(neighbor) || tentativeGCost < neighbor.getCostToNode()) {
                neighbor.setParent(current);
                neighbor.setCostToNode(tentativeGCost);
                neighbor.setHeuristicCostToGoal(manhattan(neighbor, goal));

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }
    }

    private int manhattan(Node node1, Node node2) {
        int x1 = node1.posX * cellSize;
        int y1 = node1.posY * cellSize;
        int x2 = node2.posX * cellSize;
        int y2 = node2.posY * cellSize;

        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private List<Node> getNeighbors(Node node) {
        neighbors.clear();

        int x = node.posX;
        int y = node.posY;

        int[][] directions = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (GameGrid.getInstance(gameWorld).isInGrid(newX, newY)) {
                neighbors.add(GameGrid.getInstance(gameWorld).getNode(newX, newY));
            }
        }

        return neighbors;
    }

    private List<Node> buildPath(Node node) {
        path.clear();
        while (node != null) {
            path.add(node);
            node = node.parent;
        }
        Collections.reverse(path);

        return path;
    }

    private void clear() {
        openSet.clear();
        closedSet.clear();
        GameGrid.getInstance(gameWorld).clear();
    }
}
