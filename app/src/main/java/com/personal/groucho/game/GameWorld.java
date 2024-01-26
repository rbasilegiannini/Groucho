package com.personal.groucho.game;

import static com.personal.groucho.game.Constants.grouchoPower;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.assets.Sounds.bulletHitEnemy;
import static com.personal.groucho.game.assets.Sounds.bulletHitFurniture;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.collisions.MyContactListener;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.Component;
import com.personal.groucho.game.gameobjects.components.ComponentType;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.levels.FirstLevel;
import com.personal.groucho.game.levels.Level;
import com.personal.groucho.game.controller.states.Walking;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameWorld {
    public final static int bufferWidth = 1920;
    public final static int bufferHeight = 1080;
    private final MyContactListener contactListener;
    public Bitmap buffer;
    private final Canvas canvas;
    static Box physicalSize, screenSize, currentView;
    final Activity activity;
    public World world;
    private final Physics physics;

    public final Controller controller;
    private final List<GameObject> objects;
    private GameObject player;
    private TouchHandler touchHandler;
    private Level currentLevel;
    private int currentPlayerPosX;
    private int currentPlayerPosY;


    public GameWorld(Box physicalSize, Box screenSize, Activity activity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);

        this.world = new World(0, 0); // No gravity
        physics = new Physics(physicalSize, world);
        this.controller = new Controller(
                (float) bufferWidth/2, (float) bufferHeight /2
        );

        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        this.objects = new ArrayList<>();
        this.canvas = new Canvas(buffer);
        this.currentLevel = new FirstLevel(this);
    }

    public void setPlayer(GameObject player) {
        this.player = player;
        Component component = player.getComponent(ComponentType.Position);
        if (component != null) {
            PositionComponent position = (PositionComponent) component;
            currentPlayerPosX = position.getPosX();
            currentPlayerPosY = position.getPosY();
        }
        addGameObject(player);
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        objects.add(obj);
        return obj;
    }

    public synchronized void processInputs(){
        for (Input.TouchEvent event: touchHandler.getTouchEvents()) {
            controller.consumeTouchEvent(event);
        }
    }

    public synchronized void update(float elapsedTime) {
        physics.update(elapsedTime, objects, contactListener.getCollisions());

        // Player state
        updatePlayerState();
        updateCamera();

        //
        for (GameObject gameObject : objects) {
            Component aliveComponent = gameObject.getComponent(ComponentType.Alive);
            if (aliveComponent != null){
                AliveComponent alive = (AliveComponent) aliveComponent;
                if (alive.getCurrentStatus() == DEAD)
                    handleDeath(gameObject);
            }
        }
    }

    public synchronized void render() {
        canvas.drawARGB(255,0,0,0);

        currentLevel.draw(canvas);
        drawGameObjects();
        controller.draw(canvas);

        //
        debugRayCast();
    }

    private void drawGameObjects() {
        //TODO: use a better solution
        for (GameObject gameObject : objects) {
            Component drawComponent = gameObject.getComponent(ComponentType.Drawable);
            if (drawComponent != null){
                DrawableComponent drawable = (DrawableComponent) drawComponent;
                drawable.draw(canvas);
            }
        }

        //TODO: use a better solution
        for (GameObject gameObject : objects) {
            Component lightComponent = gameObject.getComponent(ComponentType.Light);
            if (lightComponent != null){
                LightComponent light = (LightComponent) lightComponent;
                light.draw(canvas);
            }
        }
    }

    private void handleDeath(GameObject gameObject) {
//        Component spriteComponent = gameObject.getComponent(ComponentType.Drawable);
//        if (spriteComponent != null) {
//            SpriteDrawableComponent sprite = (SpriteDrawableComponent) spriteComponent;
//
//            if (sprite.isAnimationComplete()) {
//                //sprite.setIdleDeathSpritesheet();
//            }
//        }

        // Remove object
    }

    /// ONLY DEBUG
    float startX = 0;
    float startY = 0;
    float endX = 0;
    float endY = 0;

    public void debugRayCast() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }
    ///

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {
            if (event.GO1.role == Role.FURNITURE || event.GO2.role == Role.FURNITURE) {
                Log.d("GW", "Collision with furniture...");
                // Furniture collision sound
            }
        }
    }

    private void updatePlayerState() {
        Component ctrlComponent = player.getComponent(ComponentType.Controllable);
        if (ctrlComponent != null) {
            ControllableComponent controllable = (ControllableComponent) ctrlComponent;
            controllable.updatePlayerState();
        }
    }

    public void updateCamera() {
        Component component = player.getComponent(ComponentType.Position);
        if (component != null) {
            if (controller.getPlayerState().getClass().equals(Walking.class)) {
                PositionComponent position = (PositionComponent) component;
                float cameraX = currentPlayerPosX - position.getPosX();
                float cameraY = currentPlayerPosY - position.getPosY();
                moveCamera(cameraX, cameraY);
                moveController(cameraX, cameraY);

                currentPlayerPosX = position.getPosX();
                currentPlayerPosY =  position.getPosY();
            }
        }
    }

    private void moveCamera(float cameraX, float cameraY) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(cameraX, cameraY);
        canvas.concat(matrix);
    }

    private void moveController(float cameraX, float cameraY) {
        controller.updateControllerPosition(-cameraX, -cameraY);
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public void shootEvent(float originX, float originY, float endX, float endY) {
        this.startX = originX;
        this.startY = originY;
        this.endX = endX;
        this.endY = endY;

        GameObject hitGO = physics.reportGameObject(originX, originY, endX, endY);
        if (hitGO != null) {
            Log.i("RayCast", hitGO.role.name());

            switch (hitGO.role) {
                case ENEMY:
                    hitEnemyEvent(hitGO);
                    break;

                case FURNITURE:
                    hitFurnitureEvent(hitGO, originX, originY);
                    break;

                case WALL:
                    bulletHitFurniture.play(1f);
                    break;
            }
        }
    }

    private void hitEnemyEvent(GameObject hitGO) {
        bulletHitEnemy.play(1f);
        AliveComponent alive = (AliveComponent) hitGO.getComponent(ComponentType.Alive);
        if (alive.getCurrentStatus() != DEAD) alive.damage(grouchoPower);
    }

    private void hitFurnitureEvent( GameObject hitGO, float originX, float originY) {
        bulletHitFurniture.play(1f);
        PhysicsComponent physics = (PhysicsComponent) hitGO.getComponent(ComponentType.Physics);
        float goPosX = physics.getPositionX();
        float goPosY = physics.getPositionY();
        float forceX = goPosX - fromBufferToMetersX(originX);
        float forceY = goPosY - fromBufferToMetersY(originY);
        float module = (float) Math.sqrt(Math.pow(forceX,2) + Math.pow(forceY, 2));

        Vec2 force = new Vec2(20*(forceX/module), 20*(forceY/module));

        physics.applyForce(force);
    }
}
