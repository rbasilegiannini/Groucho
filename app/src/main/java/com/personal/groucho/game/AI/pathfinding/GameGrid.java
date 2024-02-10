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

    public void drawDebugGrid(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.BLUE);

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

            }
        }
    }
}
