package com.personal.groucho.game;

import static com.personal.groucho.game.Debugger.getDebugger;
import static com.personal.groucho.game.Events.alertEnemiesEvent;
import static com.personal.groucho.game.Events.playerShootEnemyEvent;
import static com.personal.groucho.game.Events.playerShootFurnitureEvent;
import static com.personal.groucho.game.Events.playerShootWallEvent;
import static com.personal.groucho.game.constants.System.debugMode;
import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.LIGHT;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;

import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.Graphics.bufferHeight;

import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.levels.Level;

public class GameWorld {
    static Box physicalSize, screenSize, currentView;
    public final MainActivity activity;
    public final Physics physics;
    public final Graphics graphics;
    public final GameObjectHandler goHandler;
    public Player player;
    public Controller controller;
    protected Level currentLevel;
    private TouchHandler touchHandler;
    private boolean pause = false;
    public boolean gameOver = false;
    public boolean complete = false;
    public boolean grouchoIsTalking = false;
    public final BubbleSpeech bubbleSpeech;

    public GameWorld(Box physicalSize, Box screenSize, MainActivity newActivity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        activity = newActivity;

        player = new Player();
        physics = Physics.getInstance(this);
        graphics = Graphics.getInstance(this);
        goHandler = GameObjectHandler.getInstance();
        bubbleSpeech = new BubbleSpeech(this);
    }

    public void init(Level level) {
        initEnvironment();
        currentLevel = level;
        initPlayer();
        currentLevel.init();
        activity.backgroundMusic.play();
    }

    private void initEnvironment() {
        clearPools();
        graphics.reset();
        controller = new Controller((float)bufferWidth/2, (float)bufferHeight /2);
        goHandler.init();
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public void initPlayer() {
        this.gameOver = false;
        GameObject playerGO = GameObjectFactory.
                makePlayer(bufferWidth /2, bufferHeight/2, controller, this);
        player.init(playerGO, this);
        goHandler.addGameObject(playerGO);
    }

    public void setPlayerVisibility(boolean visibility) {player.setPlayerVisibility(visibility);}

    public synchronized void processInputs(){
        if (!pause) {
            for (Input.TouchEvent event : touchHandler.getTouchEvents()) {
                processEvent(event);
            }
        }
    }

    private void processEvent(Input.TouchEvent event) {
        if (!gameOver) {
            if (!grouchoIsTalking) {
                if (currentLevel.eventChain.isEmpty())
                    controller.consumeTouchEvent(event);
                else
                    currentLevel.eventChain.consumeEvent();
            }
            else {
                bubbleSpeech.consumeTouchEvent(event);
            }
        }
    }

    public synchronized void update(float elapsedTime) {
        if (!pause) {
            physics.update(elapsedTime);

            if (!gameOver) {
                player.update(elapsedTime, graphics.canvas, controller);
            }

            if (complete) {
                player.reset();
            }

            for (AIComponent aiComponent : ComponentHandler.getInstance().aiComps) {
                aiComponent.update(elapsedTime, this);
            }
        }
    }

    public synchronized void render() {
        if (!pause) {
            graphics.render();

            if (grouchoIsTalking) {
                bubbleSpeech.draw(graphics.canvas);
            }

            if (debugMode) {
                getDebugger().draw(graphics.canvas);
            }
        }

        if (gameOver || complete) {
            graphics.fadeOut();
        }
    }

    public void handleDeath(GameObject character) {
        if (character.role == PLAYER) {
            GameOver();
        }
        else {
            currentLevel.handleDeath();
            ComponentHandler.getInstance().removeComponent(character, AI);
            ComponentHandler.getInstance().removeComponent(character, PHYSICS);
            ComponentHandler.getInstance().removeComponent(character, LIGHT);
        }
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

    public void resume() {
        activity.backgroundMusic.play();
        pause = false;
    }
    public void pause() {
        activity.backgroundMusic.pause();
        pause = true;
    }

    public void GameOver() {
        player.reset();
        this.gameOver = true;

        ComponentHandler.getInstance().removeComponent(player.gameObject, CONTROLLABLE);
        ComponentHandler.getInstance().removeComponent(player.gameObject, PHYSICS);
        ComponentHandler.getInstance().removeComponent(player.gameObject, LIGHT);
    }

    protected void finalize(){
        try {
            super.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        physics.finalize();
        graphics.finalize();
        goHandler.finalize();

        clearPools();

        activity.finish();
    }

    protected void clearPools() {
        GameGrid.getInstance().releasePool();
        goHandler.clear();
    }

    public void hasToTalk() {
        grouchoIsTalking = true;
        player.rest();
    }
}
