package com.personal.groucho.game.AI.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class GameGrid {
    private final int width;
    private final int height;
    private final int cellSize;
    private final Node[][] grid;

    public GameGrid(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.grid = new Node[width][height];

        for (int posX = 0; posX < width; posX++) {
            for (int posY = 0; posY < height; posY++) {
                grid[posX][posY] = new Node(posX, posY);
            }
        }
    }

    public Node getNode(int posX, int posY) {return grid[posX][posY];}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getCellSize() {return cellSize;}

    public boolean isInGrid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void reset() {
        for (int posX = 0; posX < width; posX++) {
            for (int posY = 0; posY < height; posY++) {
                grid[posX][posY].reset();
            }
        }
    }

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        int x = node.getPosX();
        int y = node.getPosY();

        int[][] directions = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (isInGrid(newX, newY)) {
                neighbors.add(getNode(newX, newY));
            }
        }

        return neighbors;
    }
}
