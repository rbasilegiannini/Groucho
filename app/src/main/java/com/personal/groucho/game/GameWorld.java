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
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.gameobjects.GameObjectFactory;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.BoxDrawableComponent;
import com.personal.groucho.game.gameobjects.components.CharacterComponent;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.gameobjects.components.SpriteComponent;
import com.personal.groucho.game.gameobjects.components.TextureDrawableComponent;
import com.personal.groucho.game.gameobjects.components.TriggerComponent;
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

    // Pools to reduce allocation and de-allocation
    public final ObjectsPool<GameObject> objectsPool = new ObjectsPool<>(100, GameObject.class);
    public final ObjectsPool<Collision> collisionsPool = new ObjectsPool<>(30, Collision.class);
    public final ObjectsPool<Node> nodesPool = new ObjectsPool<>(200, Node.class);
    public final ObjectsPool<PositionComponent> posCompPool = new ObjectsPool<>(100, PositionComponent.class);
    public final ObjectsPool<PhysicsComponent> phyCompPool = new ObjectsPool<>(100, PhysicsComponent.class);
    public final ObjectsPool<AIComponent> aiCompPool = new ObjectsPool<>(10, AIComponent.class);
    public final ObjectsPool<SpriteComponent> spriteCompPool = new ObjectsPool<>(100, SpriteComponent.class);
    public final ObjectsPool<TextureDrawableComponent> textureCompPool = new ObjectsPool<>(100, TextureDrawableComponent.class);
    public final ObjectsPool<BoxDrawableComponent> boxCompPool = new ObjectsPool<>(100, BoxDrawableComponent.class);
    public final ObjectsPool<AliveComponent> aliveCompPool = new ObjectsPool<>(100, AliveComponent.class);
    public final ObjectsPool<CharacterComponent> charCompPool = new ObjectsPool<>(100, CharacterComponent.class);
    public final ObjectsPool<LightComponent> lightCompPool = new ObjectsPool<>(1, LightComponent.class);
    public final ObjectsPool<ControllableComponent> ctrlCompPool = new ObjectsPool<>(1, ControllableComponent.class);
    public final ObjectsPool<TriggerComponent> triggerCompPool = new ObjectsPool<>(30, TriggerComponent.class);

    public GameWorld(Box physicalSize, Box screenSize, MainActivity newActivity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        activity = newActivity;

        player = new Player();  // TODO: Singleton?
        physics = Physics.getInstance(this);
        graphics = Graphics.getInstance(this);
        goHandler = GameObjectHandler.getInstance(this);
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
        GameGrid.getInstance(this).releasePool();

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
                if (!gameOver && !grouchoIsTalking) {
                    controller.consumeTouchEvent(event);
                }
                else {
                    if (grouchoIsTalking) {
                        bubbleSpeech.consumeTouchEvent(event);
                    }
                }
            }
        }
    }

    public synchronized void update(float elapsedTime) {
        if (!pause) {
            physics.update(elapsedTime);

            if (!gameOver) {
                player.update(graphics.canvas, controller);
            }

            for (AIComponent aiComponent : ComponentHandler.getInstance(this).aiComps) {
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
                getDebugger(this).draw(graphics.canvas);
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
            ComponentHandler.getInstance(this).removeComponent(gameObject, AI);
            ComponentHandler.getInstance(this).removeComponent(gameObject, PHYSICS);
            ComponentHandler.getInstance(this).removeComponent(gameObject, LIGHT);
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

        ComponentHandler.getInstance(this).removeComponent(player.gameObject, CONTROLLABLE);
        ComponentHandler.getInstance(this).removeComponent(player.gameObject, PHYSICS);
        ComponentHandler.getInstance(this).removeComponent(player.gameObject, LIGHT);
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
