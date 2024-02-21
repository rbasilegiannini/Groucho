package com.personal.groucho.game;

import static com.personal.groucho.game.Debugger.getDebugger;
import static com.personal.groucho.game.Events.alertEnemiesEvent;
import static com.personal.groucho.game.Events.gameOverEvent;
import static com.personal.groucho.game.Events.playerShootEnemyEvent;
import static com.personal.groucho.game.Events.playerShootFurnitureEvent;
import static com.personal.groucho.game.Events.playerShootWallEvent;
import static com.personal.groucho.game.constants.System.debugMode;
import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.LIGHT;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import android.graphics.Bitmap;

import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.Graphics.bufferHeight;

import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.levels.Level;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameWorld {
    static Box physicalSize, screenSize, currentView;
    final MainActivity activity;
    private final Physics physics;
    private final Graphics graphics;
    private Player player;
    public Controller controller;
    protected Level currentLevel;
    protected GameGrid grid;
    private final List<GameObject> objects = new ArrayList<>();
    protected final List<PositionComponent> posComponents = new ArrayList<>();
    protected final List<PhysicsComponent> phyComponents = new ArrayList<>();
    protected final List<DrawableComponent> drawComponents = new ArrayList<>();
    protected final List<AIComponent> aiComponents = new ArrayList<>();
    protected final List<AliveComponent> aliveComponents = new ArrayList<>();
    protected final List<LightComponent> lightComponents = new ArrayList<>();
    private TouchHandler touchHandler;
    private boolean pause = false;
    protected boolean gameOver = false;

    public GameWorld(Box physicalSize, Box screenSize, MainActivity newActivity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        activity = newActivity;

        physics = new Physics(this);
        graphics = Graphics.getInstance(this);
    }

    public void init(Level level) {
        graphics.reset();
        controller = new Controller((float)bufferWidth/2, (float)bufferHeight /2);

        posComponents.clear();
        phyComponents.clear();
        aliveComponents.clear();
        lightComponents.clear();
        drawComponents.clear();
        aiComponents.clear();

        Iterator<GameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            GameObject go = iterator.next();
            go.delete();
            iterator.remove();
        }

        currentLevel = level;
        currentLevel.init();
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public void setPlayer(int posX, int posY) {
        this.gameOver = false;
        GameObject playerGO = GameObjectFactory.
                makePlayer(bufferWidth /2, bufferHeight/2, controller, this);
        PositionComponent posComponent = (PositionComponent) playerGO.getComponent(POSITION);

        player = new Player(playerGO, posComponent.posX, posComponent.posY);
        player.setPos(posX, posY);

        addGameObject(playerGO);
    }
    public void setGameGrid(GameGrid grid) {
        this.grid = grid;
        physics.setGameGrid(grid);
    }
    public void setPlayerVisibility(boolean visibility) {player.setPlayerVisibility(visibility);}
    public boolean isPlayerVisible() {return player.isPlayerVisible;}
    public Bitmap getBuffer() {return graphics.buffer;}
    public World getWorld() {return physics.world;}
    public Level getLevel() {return currentLevel;}
    public GameObject getPlayerGO(){return player.gameObject;}
    public float getPlayerPositionX() {return player.posX;}
    public float getPlayerPositionY() {return player.posY;}

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

    public synchronized void addGameObject(GameObject go) {
        objects.add(go);

        Component posComponent = go.getComponent(POSITION);
        Component drawComponent = go.getComponent(DRAWABLE);
        Component phyComponent = go.getComponent(PHYSICS);
        Component aliveComponent = go.getComponent(ALIVE);
        Component aiComponent = go.getComponent(AI);
        Component lightComponent = go.getComponent(LIGHT);

        if (posComponent != null) posComponents.add((PositionComponent) posComponent);
        if (drawComponent != null) drawComponents.add((DrawableComponent) drawComponent);
        if (phyComponent != null) phyComponents.add((PhysicsComponent) phyComponent);
        if (aliveComponent != null) aliveComponents.add((AliveComponent) aliveComponent);
        if (aiComponent != null) aiComponents.add((AIComponent) aiComponent);
        if (lightComponent != null) lightComponents.add((LightComponent) lightComponent);
    }

    public synchronized void processInputs(){
        if (!pause) {
            for (Input.TouchEvent event : touchHandler.getTouchEvents()) {
                if (!gameOver) {
                    controller.consumeTouchEvent(event);
                }
            }
        }
    }

    public synchronized void update(float elapsedTime) {
        if (!pause) {
            physics.update(elapsedTime);

            if (!gameOver) {
                for (AliveComponent aliveComponent : aliveComponents) {
                    if (aliveComponent.currentStatus == DEAD) {
                        handleDeath((GameObject) aliveComponent.getOwner());
                    }
                }
            }

            player.update(graphics.canvas, controller);

            for (AIComponent aiComponent : aiComponents) {
                aiComponent.update(this);
            }
        }
    }

    public synchronized void render() {
        if (!pause) {
            graphics.render();
            if (debugMode) {
                getDebugger(this).draw(graphics.canvas);
            }
        }
        if (gameOver) {
            graphics.fadeOut();
        }
    }

    private void handleDeath(GameObject gameObject) {
        if (gameObject.role == PLAYER) {
            gameOverEvent(this);
        }
        else {
            removeComponent(gameObject, AI);
            removeComponent(gameObject, PHYSICS);
            removeComponent(gameObject, LIGHT);
        }
    }

    private void removeComponent(GameObject go, ComponentType type){
        Component component = go.getComponent(type);

        // TODO: Use a map
        switch (type) {
            case AI:
                aiComponents.remove((AIComponent)component);
                break;
            case ALIVE:
                aliveComponents.remove((AliveComponent)component);
                break;
            case LIGHT:
                lightComponents.remove((LightComponent)component);
                break;
            case PHYSICS:
                phyComponents.remove((PhysicsComponent)component);
                break;
            case DRAWABLE:
                drawComponents.remove((DrawableComponent)component);
                break;
            case POSITION:
                posComponents.remove((PositionComponent)component);
                break;
        }

        go.removeComponent(type);
    }

    public void shootEvent(float originX, float originY, float endX, float endY) {
        alertEnemiesEvent(this);
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
        posComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        phyComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        aliveComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        lightComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        drawComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        aiComponents.clear();

        Iterator<GameObject> iterator = objects.iterator();
        while (iterator.hasNext()) {
            GameObject go = iterator.next();
            if (go.role != PLAYER) {
                go.delete();
                iterator.remove();
            }
        }

        currentLevel = newLevel;
        currentLevel.init();

        if(debugMode) {
            getDebugger(this).updateDebugger();
        }
    }

    public void resume() {
        pause = false;
    }

    public void pause() {
        pause = true;
    }

    protected void finalize() {
        physics.finalize();
    }

    public void GameOver() {
        this.gameOver = true;

        removeComponent(player.gameObject, CONTROLLABLE);
        removeComponent(player.gameObject, PHYSICS);
        removeComponent(player.gameObject, LIGHT);
    }

    public boolean isGameOver() {return gameOver;}
}
