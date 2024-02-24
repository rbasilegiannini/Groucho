package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.Utils.toBufferXLength;
import static com.personal.groucho.game.Utils.toBufferYLength;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.personal.groucho.game.AI.Sight;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.ArrayList;
import java.util.List;

public class Debugger {
    private final GameWorld gameWorld;
    private final List<Sight> sights = new ArrayList<>();
    private GameGrid grid;
    private static Debugger instance = null;
    private final Paint positionPaint, colliderPaint, gameGridPaint, sightPaint, pathPaint;

    private Debugger(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        colliderPaint = new Paint();
        colliderPaint.setStyle(Paint.Style.STROKE);
        colliderPaint.setColor(Color.RED);

        gameGridPaint = new Paint();
        gameGridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gameGridPaint.setTextSize(20);

        positionPaint = new Paint();
        positionPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        positionPaint.setColor(Color.GREEN);

        sightPaint = new Paint();
        sightPaint.setColor(Color.RED);
        sightPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pathPaint.setColor(Color.BLUE);

        updateDebugger();
    }

    public static Debugger getDebugger(GameWorld gameWorld) {
        if (instance == null) {
            instance = new Debugger(gameWorld);
        }
        return instance;
    }

    public void draw(Canvas canvas) {
        grid.drawDebugGrid(canvas, gameGridPaint);

        for (AIComponent aiComponent : gameWorld.aiComponents) {
            aiComponent.getSight().drawDebugSight(canvas, sightPaint);
            aiComponent.drawDebugPath(canvas, pathPaint);
        }

        drawPositions(canvas);
        drawColliders(canvas);
    }

    private void drawPositions(Canvas canvas) {
        for (PositionComponent posComponent : gameWorld.posComponents) {
            canvas.drawCircle(posComponent.posX, posComponent.posY, 20, positionPaint);
        }
    }

    private void drawColliders(Canvas canvas){
        for (PhysicsComponent phyComponent : gameWorld.phyComponents) {
            float shapeCenterX = toBufferXLength(phyComponent.fixtureCenterX);
            float shapeCenterY = toBufferYLength(phyComponent.fixtureCenterY);

            float left = shapeCenterX + fromMetersToBufferX(phyComponent.getPosX()) - phyComponent.dimX/2;
            float top = shapeCenterY + fromMetersToBufferY(phyComponent.getPosY()) - phyComponent.dimY/2;
            float right = left + phyComponent.dimX;
            float bottom = top + phyComponent.dimY;

            canvas.drawRect(
                    left,
                    top,
                    right,
                    bottom,
                    colliderPaint
            );
        }
    }

    public void updateDebugger() {
        sights.clear();
        for (AIComponent aiComponent : gameWorld.aiComponents) {
            sights.add(aiComponent.getSight());
        }
        grid = gameWorld.grid;
    }
}
