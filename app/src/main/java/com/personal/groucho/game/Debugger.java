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
    private final Paint paint, colliderPaint;

    private Debugger(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        paint = new Paint();
        colliderPaint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        colliderPaint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        colliderPaint.setColor(Color.RED);

        updateDebugger();
    }

    public static Debugger getDebugger(GameWorld gameWorld) {
        if (instance == null) {
            instance = new Debugger(gameWorld);
        }
        return instance;
    }

    public void draw(Canvas canvas) {
        for (Sight sight : sights) {
            sight.drawDebugSight(canvas);
        }
        grid.drawDebugGrid(canvas);

        drawPositions(canvas);
        drawColliders(canvas);
    }

    private void drawPositions(Canvas canvas) {
        for (PositionComponent posComponent : gameWorld.posComponents) {
            canvas.drawCircle(posComponent.posX, posComponent.posY, 20, paint);
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
