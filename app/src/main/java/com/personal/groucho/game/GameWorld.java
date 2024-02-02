package com.personal.groucho.game;

import static com.personal.groucho.game.Constants.grouchoPower;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.assets.Sounds.bulletHitEnemy;
import static com.personal.groucho.game.assets.Sounds.bulletHitFurniture;
import static com.personal.groucho.game.gameobjects.Status.DEAD;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.badlogic.androidgames.framework.impl.TouchHandler;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.Graphics.bufferHeight;

import com.personal.groucho.game.gameobjects.Sight;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.levels.FirstLevel;
import com.personal.groucho.game.levels.Level;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {
    static Box physicalSize, screenSize, currentView;
    final Activity activity;
    private final Physics physics;
    private final Graphics graphics;
    private Player player;
    public final Controller controller;
    private Level currentLevel;
    private final List<GameObject> objects;
    private TouchHandler touchHandler;

    public GameWorld(Box physicalSize, Box screenSize, Activity newActivity) {
        GameWorld.physicalSize = physicalSize;
        GameWorld.screenSize = screenSize;
        currentView = physicalSize;
        activity = newActivity;

        physics = new Physics(physicalSize);
        controller = new Controller((float)bufferWidth/2, (float)bufferHeight /2);

        graphics = new Graphics();
        objects = new ArrayList<>();
        currentLevel = new FirstLevel(this);
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    public void setPlayer(GameObject player) {
        Component component = player.getComponent(ComponentType.Position);
        if (component != null) {
            PositionComponent position = (PositionComponent) component;
            this.player = new Player(player, position.getPosX(), position.getPosY());
        }
        addGameObject(player);
    }

    public Bitmap getBuffer() {return graphics.getBuffer();}
    public World getWorld() {return physics.getWorld();}
    public Vec2 getPlayerPosition() {return player.getPosition();}

    public synchronized void addGameObject(GameObject obj) {objects.add(obj);}
    public synchronized void removeGameObject(GameObject gameObject) {objects.remove(gameObject);}

    public synchronized void processInputs(){
        for (Input.TouchEvent event: touchHandler.getTouchEvents()) {
            controller.consumeTouchEvent(event);
        }
    }

    public synchronized void update(float elapsedTime) {
        physics.update(elapsedTime, objects);
        player.update(graphics.getCanvas(), controller);

        //TODO: Use a better solution
        for (GameObject gameObject : objects) {
            Component aliveComponent = gameObject.getComponent(ComponentType.Alive);
            if (aliveComponent != null){
                AliveComponent alive = (AliveComponent) aliveComponent;
                if (alive.getCurrentStatus() == DEAD)
                    handleDeath(gameObject);
            }
        }

        //TODO: Use a better solution
        for (GameObject gameObject : objects) {
            Component component = gameObject.getComponent(ComponentType.AI);
            if (component != null){
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
        if (sight != null)
            sight.drawDebugRayCast(graphics.getCanvas());
    }

    private void handleDeath(GameObject gameObject) {
        gameObject.removeComponent(ComponentType.Physics);
        gameObject.removeComponent(ComponentType.Position);
        gameObject.removeComponent(ComponentType.Alive);
    }

    public void shootEvent(float originX, float originY, float endX, float endY) {
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

    public boolean getTestTransition() {
        if (player.getPosition().getX() >= 600 &&
                player.getPosition().getX() <= 700)
            return true;
        return false;
    }
}
