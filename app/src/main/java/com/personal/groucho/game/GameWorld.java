package com.personal.groucho.game;

import static com.personal.groucho.game.Events.gameOverEvent;
import static com.personal.groucho.game.Events.playerShootEnemyEvent;
import static com.personal.groucho.game.Events.playerShootFurnitureEvent;
import static com.personal.groucho.game.Events.playerShootWallEvent;
import static com.personal.groucho.game.constants.System.debugMode;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.Graphics.bufferHeight;

import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.AI.Sight;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.levels.FirstLevel;
import com.personal.groucho.game.levels.Level;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld {
    static Box physicalSize, screenSize, currentView;
    final Activity activity;
    private final Physics physics;
    private final Graphics graphics;
    private Player player;
    public final Controller controller;
    private Level currentLevel;
    private GameGrid grid;
    private final List<GameObject> objects = new ArrayList<>();
    private TouchHandler touchHandler;
    private boolean gameOver = false;

    public GameWorld(Box physicalSize, Box screenSize, Activity newActivity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        activity = newActivity;

        physics = new Physics(this);
        controller = new Controller((float)bufferWidth/2, (float)bufferHeight /2);
        graphics = new Graphics(this);
        setPlayer();

        currentLevel = new FirstLevel(this);
        currentLevel.init();
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    private void setPlayer() {
        GameObject player = GameObjectFactory.
                makePlayer(
                        Graphics.bufferWidth /2,
                        Graphics.bufferHeight/2,
                        controller,
                        this
                );

        Component component = player.getComponent(ComponentType.POSITION);
        if (component != null) {
            PositionComponent position = (PositionComponent) component;
            this.player = new Player(player, position.getPosX(), position.getPosY());
        }

        addGameObject(player);
    }
    public void setGameGrid(GameGrid grid) {
        this.grid = grid;
        physics.setGameGrid(grid);
    }
    public void setPlayerVisibility(boolean visibility) {player.setPlayerVisibility(visibility);}
    public boolean isPlayerVisible() {return player.getPlayerVisibility();}
    public Bitmap getBuffer() {return graphics.getBuffer();}
    public World getWorld() {return physics.getWorld();}
    public Level getLevel() {return currentLevel;}
    public GameObject getPlayerGO(){return player.getGameObject();}
    public Vec2 getPlayerPosition() {return player.getPosition();}
    public GameGrid getGameGrid() {return grid;}
    public List<GameObject> getGOByRole(Role role) {
        List<GameObject> gameObjects = new ArrayList<>();
        for (GameObject go : objects) {
            if (go.role == role) {
                gameObjects.add(go);
            }
        }
        return gameObjects;
    }

    public synchronized void addGameObject(GameObject obj) {objects.add(obj);}
    public synchronized void removeGameObject(GameObject gameObject) {objects.remove(gameObject);}

    public synchronized void processInputs(){

        for (Input.TouchEvent event: touchHandler.getTouchEvents()) {
            if (!gameOver) {
                controller.consumeTouchEvent(event);
            }
//            else {
//                menu.consumeTouchEvent(event);
//            }
        }
    }

    public synchronized void update(float elapsedTime) {
        physics.update(elapsedTime, objects);

        if (!gameOver) {
            //TODO: Use a better solution
            for (GameObject gameObject : objects) {
                Component aliveComponent = gameObject.getComponent(ComponentType.ALIVE);
                if (aliveComponent != null) {
                    AliveComponent alive = (AliveComponent) aliveComponent;
                    if (alive.getCurrentStatus() == DEAD) {
                        handleDeath(gameObject);
                    }
                }
            }
        }

        player.update(graphics.getCanvas(), controller);

        //TODO: Use a better solution
        for (GameObject gameObject : objects) {
            Component component = gameObject.getComponent(ComponentType.AI);
            if (component != null) {
                AIComponent ai = (AIComponent) component;
                ai.update(this);

                //
                if (sight == null) sight = ai.getSight();
            }
        }
    }

    //
    Sight sight = null;
    //
    public synchronized void render() {
        graphics.render(objects, currentLevel, controller);

        //
        if (debugMode) {
            sight.drawDebugRayCast(graphics.getCanvas());
            grid.drawDebugGrid(graphics.getCanvas());
        }
    }

    private void handleDeath(GameObject gameObject) {
        if (gameObject.role == Role.PLAYER) {
            gameOverEvent(this);
        }
        else {
            gameObject.removeComponent(ComponentType.AI);
            gameObject.removeComponent(ComponentType.PHYSICS);
            gameObject.removeComponent(ComponentType.POSITION);
            gameObject.removeComponent(ComponentType.ALIVE);
        }
    }

    public void shootEvent(float originX, float originY, float endX, float endY) {
        GameObject hitGO = physics.reportGameObject(originX, originY, endX, endY);
        if (hitGO != null) {
            switch (hitGO.role) {
                case ENEMY:
                    playerShootEnemyEvent(hitGO);
                    break;

                case FURNITURE:
                    playerShootFurnitureEvent(hitGO, originX, originY);
                    break;

                case WALL:
                    playerShootWallEvent();
                    break;
            }
        }
    }

    public synchronized void changeLevel(Level newLevel) {
        Iterator<GameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            GameObject go = iterator.next();
            if (go.role != Role.PLAYER) {
                go.delete();
                iterator.remove();
            }
        }

        currentLevel = newLevel;
        currentLevel.init();
    }

    public void GameOver() {
        this.gameOver = true;
        // Game over menu
    }

    public boolean isGameOver() {return gameOver;}

}
