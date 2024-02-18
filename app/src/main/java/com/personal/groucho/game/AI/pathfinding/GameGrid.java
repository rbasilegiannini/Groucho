package com.personal.groucho.game.AI.pathfinding;

import static com.personal.groucho.game.constants.System.cellSize;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.HashSet;
import java.util.Set;

public class GameGrid {
    private final int width;
    private final int height;
    private final Node[][] grid;

    public GameGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Node[width][height];

        for (int posX = 0; posX < width; posX++) {
            for (int posY = 0; posY < height; posY++) {
                grid[posX][posY] = new Node(posX, posY);
            }
        }
    }

    public Node getNode(int posX, int posY) {return grid[posX][posY];}
    public Set<Node> getNodes(int centerX, int centerY, int dimX, int dimY) {
        Set<Node> nodes = new HashSet<>();

        int top = (centerY + dimY/2)/cellSize;
        int left = (centerX - dimX/2)/cellSize;
        int right = (centerX + dimX/2)/cellSize;
        int bottom = (centerY - dimY/2)/cellSize;

        for (int y = bottom; y <= top; y++){
            if(isInGrid(left, y)){
                nodes.add(grid[left][y]);
            }
            if(isInGrid(right, y)){
                nodes.add(grid[right][y]);
            }
        }

        for (int x = left; x <= right; x++){
            if(isInGrid(x, bottom)){
                nodes.add(grid[x][bottom]);
            }
            if(isInGrid(x, top)) {
                nodes.add(grid[x][top]);
            }
        }

        return nodes;
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

    public void reset() {
        for (int posX = 0; posX < width; posX++) {
            for (int posY = 0; posY < height; posY++) {
                grid[posX][posY].reset();
            }
        }
    }

    public void drawDebugGrid(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(20);

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
