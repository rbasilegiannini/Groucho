package com.personal.groucho.game.AI.pathfinding;

import static com.personal.groucho.game.constants.System.cellSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar {
    private final GameGrid grid;
    private final PriorityQueue<Node> openSet;
    private final Set<Node> closedSet;
    private final List<Node> neighbors = new ArrayList<>();
    private final List<Node> path = new ArrayList<>();


    public AStar(GameGrid grid) {
        this.grid = grid;
        this.openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
        this.closedSet = new HashSet<>();
    }

    public List<Node> findPath(Node start, Node goal) {
        reset();

        openSet.add(start);
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.equal(goal))
                return buildPath(current);

            processNeighbors(current, goal);
        }

        return Collections.emptyList(); // No path found
    }

    private void processNeighbors(Node current, Node goal) {
        closedSet.add(current);
        for (Node neighbor : getNeighbors(current)) {
            if (closedSet.contains(neighbor))
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

            if (grid.isInGrid(newX, newY)) {
                neighbors.add(grid.getNode(newX, newY));
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

    private void reset() {
        openSet.clear();
        closedSet.clear();
        grid.reset();
    }
}
