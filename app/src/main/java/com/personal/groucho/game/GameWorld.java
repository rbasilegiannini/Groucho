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
import com.personal.groucho.game.levels.first.GrouchoRoom;
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
    public boolean grouchoIsTalking = false;
    public final BubbleSpeech bubbleSpeech;

    public GameWorld(Box physicalSize, Box screenSize, MainActivity newActivity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        activity = newActivity;

        player = new Player();  // TODO: Singleton?
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
    }

    public void tryAgain(Level level) {
        initEnvironment();
        GrouchoRoom.firstTime = true;
        GameGrid.getInstance().releasePool();

        currentLevel = level;
        initPlayer();
        currentLevel.init();
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
                player.update(graphics.canvas, controller);
            }

            for (AIComponent aiComponent : ComponentHandler.getInstance().aiComps) {
                aiComponent.update(this);
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

        if (gameOver) {
            graphics.fadeOut();
        }
    }

    public void handleDeath(GameObject gameObject) {
        if (gameObject.role == PLAYER) {
            GameOver();
        }
        else {
            ComponentHandler.getInstance().removeComponent(gameObject, AI);
            ComponentHandler.getInstance().removeComponent(gameObject, PHYSICS);
            ComponentHandler.getInstance().removeComponent(gameObject, LIGHT);
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

    public synchronized void changeLevel(Level newLevel) {
//        goHandler.changeLevel();
//        nodesPool.clear();
//        grid.reset();
//
//        currentLevel = newLevel;
//        currentLevel.init();
    }

    public void resume() {pause = false;}
    public void pause() {pause = true;}

    public void GameOver() {
        player.GameOver();
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
        goHandler.clear();
    }

    public void hasToTalk() {
        grouchoIsTalking = true;
        player.rest();
    }
}
