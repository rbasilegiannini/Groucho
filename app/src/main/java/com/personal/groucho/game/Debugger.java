package com.personal.groucho.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.personal.groucho.game.AI.Sight;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.ArrayList;
import java.util.List;

public class Debugger {
    private final GameWorld gameWorld;
    private final List<Sight> sights = new ArrayList<>();
    private GameGrid grid;
    private static Debugger instance = null;

    private Debugger(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
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
    }

    private void drawPositions(Canvas canvas) {
        // Debug
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GREEN);

        for (PositionComponent posComponent : gameWorld.posComponents) {
            canvas.drawCircle(posComponent.getPosX(), posComponent.getPosY(), 20, paint);
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
