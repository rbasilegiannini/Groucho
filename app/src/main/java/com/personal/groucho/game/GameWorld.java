package com.personal.groucho.game;

import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import com.personal.groucho.game.components.Component;
import com.personal.groucho.game.components.ComponentType;
import com.personal.groucho.game.components.ControllableComponent;
import com.personal.groucho.game.components.DrawableComponent;
import com.personal.groucho.game.components.PhysicsComponent;
import com.personal.groucho.game.components.PositionComponent;
import com.personal.groucho.game.levels.FirstLevel;
import com.personal.groucho.game.levels.Level;
import com.personal.groucho.game.states.Walking;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.RayCastCallback;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameWorld {
    public final static int bufferWidth = 1920;
    public final static int bufferHeight = 1080;
    private final MyContactListener contactListener;
    Bitmap buffer;
    private final Canvas canvas;
    static Box physicalSize, screenSize, currentView;
    final Activity activity;
    public World world;
    final Controller controller;
    private List<GameObject> objects;
    private GameObject player;
    private TouchHandler touchHandler;
    private Level currentLevel;
    private int currentPlayerPosX;
    private int currentPlayerPosY;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    public GameWorld(Box physicalSize, Box screenSize, Activity activity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);

        this.world = new World(0, 0); // No gravity
        this.controller = new Controller((float) 200, (float) bufferHeight /2, this);
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
        // Physics
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        handleCollisions(contactListener.getCollisions());
        updatePhysicsPosition();

        // Player state
        updatePlayerState();
        updateCamera();
    }

    public synchronized void render() {
        canvas.drawARGB(255,0,0,0);
        currentLevel.draw(canvas);
        for (GameObject gameObject : objects) {
            Component component = gameObject.getComponent(ComponentType.Drawable);
            if (component != null) {
                DrawableComponent drawable = (DrawableComponent) component;
                drawable.draw(canvas);
            }
        }
        controller.draw(canvas);

        //
        debugRayCast();
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

    private void updatePhysicsPosition() {
        // TODO: use a better solution
        for (GameObject go : objects) {
            Component phyComponent = go.getComponent(ComponentType.Physics);
            Component posComponent = go.getComponent(ComponentType.Position);
            if (phyComponent != null && posComponent != null) {
                PhysicsComponent physicsComponent = (PhysicsComponent) phyComponent;
                PositionComponent positionComponent = (PositionComponent) posComponent;
                positionComponent.setPosX(
                        (int) fromMetersToBufferX(physicsComponent.getPositionX())
                );
                positionComponent.setPosY(
                        (int) fromMetersToBufferY(physicsComponent.getPositionY())
                );
            }
        }
    }

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event: collisions) {
            Log.d("GG", "Collision...");
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
        this.startX = fromMetersToBufferX(originX);
        this.startY = fromMetersToBufferY(originY);
        this.endX = fromMetersToBufferX(endX);
        this.endY = fromMetersToBufferY(endY);

        world.rayCast(
                new RayCastCallback() {
                    public float reportFixture(Fixture fixture, Vec2 i, Vec2 n, float f) {
                        // Fixture hitFixture = fixture;
                        // frictions.add(f);
                        // ...
                        // Maybe i have to use a collection of (Role, friction)
                        // and sorting from less friction.
                        // if (first == Role.ENEMY) kill it;
                        // ...but not in this callback.
                        Log.i("GW", "hit");
                        return f;
                    }
                },
                originX, originY, endX, endY
        );
    }
}
