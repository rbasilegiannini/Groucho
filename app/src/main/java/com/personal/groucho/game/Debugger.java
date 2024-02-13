package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.Role.ENEMY;

import android.graphics.Canvas;

import com.personal.groucho.game.AI.Sight;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.AIComponent;

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
    }

    public void updateDebugger() {
        sights.clear();
        for (GameObject enemy : gameWorld.getGOByRole(ENEMY)) {
            AIComponent aiComponent = (AIComponent) enemy.getComponent(AI);
            sights.add(aiComponent.getSight());
        }
        grid = gameWorld.getGameGrid();
    }
}
