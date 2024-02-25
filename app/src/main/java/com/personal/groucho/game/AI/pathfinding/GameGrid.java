package com.personal.groucho.game.AI.pathfinding;

import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;

import com.personal.groucho.game.GameWorld;

public class GameGrid {
    public final int width;
    public final int height;
    public final Node[][] grid;
    private final SparseArray<Node> nodesBelowTheObject = new SparseArray<>();

    public GameGrid(int width, int height, GameWorld gameWorld) {
        this.width = width;
        this.height = height;
        this.grid = new Node[width][height];

        for (int posX = 0; posX < width; posX++) {
            for (int posY = 0; posY < height; posY++) {
                grid[posX][posY] = gameWorld.nodesPool.acquire();
                grid[posX][posY].posX = posX;
                grid[posX][posY].posY = posY;
            }
        }
    }

    public Node getNode(int posX, int posY) {return grid[posX][posY];}
    
    public SparseArray<Node> getNodes(int centerX, int centerY, int dimX, int dimY) {
        nodesBelowTheObject.clear();

        int top = (centerY + dimY/2)/cellSize;
        int left = (centerX - dimX/2)/cellSize;
        int right = (centerX + dimX/2)/cellSize;
        int bottom = (centerY - dimY/2)/cellSize;

        for (int y = bottom; y <= top; y++){
            if(isInGrid(left, y)){
                nodesBelowTheObject.put(grid[left][y].hashCode(), grid[left][y]);
            }
            if(isInGrid(right, y)){
                nodesBelowTheObject.put(grid[right][y].hashCode(), grid[right][y]);
            }
        }

        for (int x = left; x <= right; x++){
            if(isInGrid(x, bottom)){
                nodesBelowTheObject.put(grid[x][bottom].hashCode(), grid[x][bottom]);
            }
            if(isInGrid(x, top)) {
                nodesBelowTheObject.put(grid[x][top].hashCode(), grid[x][top]);
            }
        }

        return nodesBelowTheObject;
    }

    public boolean isInGrid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void increaseDefaultCostOnNode(Node node, int increase) {
        grid[node.posX][node.posY].defaultCost += increase;
    }

    public void decreaseDefaultCostOnNode(Node node, int decrease) {
        grid[node.posX][node.posY].defaultCost -= decrease;
    }

    public void clear() {
        for (int posX = 0; posX < width; posX++) {
            for (int posY = 0; posY < height; posY++) {
                grid[posX][posY].clear();
            }
        }
    }

    public void drawDebugGrid(Canvas canvas, Paint paint){
        int startX;
        int startY;
        int endX;
        int endY;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                startX = x * cellSize;
                startY = y*cellSize;

                // Vertical lines
                endX = startX;
                endY = startY + cellSize;
                canvas.drawLine(startX, startY, endX, endY, paint);

                // Horizontal lines
                endX = startX + cellSize;
                endY = startY;
                canvas.drawLine(startX, startY, endX, endY, paint);

                String cost = String.valueOf(grid[x][y].defaultCost);
                paint.setColor(Color.WHITE);
                canvas.drawText(cost, startX+(float)cellSize /4, startY+ (float)cellSize /2, paint);
            }
        }
    }
}
