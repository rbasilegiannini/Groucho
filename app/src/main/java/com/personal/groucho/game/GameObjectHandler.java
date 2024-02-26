package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.LIGHT;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;

import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;

import java.util.ArrayList;
import java.util.List;

public class GameObjectHandler {
    private static GameObjectHandler instance = null;
    private final GameWorld gameWorld;
    protected final List<GameObject> objects = new ArrayList<>();
    protected final List<PositionComponent> posComponents = new ArrayList<>();
    protected final List<PhysicsComponent> phyComponents = new ArrayList<>();
    protected final List<DrawableComponent> drawComponents = new ArrayList<>();
    protected final List<AIComponent> aiComponents = new ArrayList<>();
    protected final List<AliveComponent> aliveComponents = new ArrayList<>();
    protected final List<LightComponent> lightComponents = new ArrayList<>();

    private GameObjectHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public static GameObjectHandler getInstance(GameWorld gameWorld) {
        if (instance == null) {
            instance = new GameObjectHandler(gameWorld);
        }
        return instance;
    }

    public void init() {
        posComponents.clear();
        phyComponents.clear();
        aliveComponents.clear();
        lightComponents.clear();
        drawComponents.clear();
        aiComponents.clear();

        for (int i = 0; i < objects.size(); i++) {
            gameWorld.objectsPool.release(objects.get(i));
        }
        objects.clear();
    }

    public void addGameObject(GameObject go) {
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

    public void removeGameObject(GameObject go){
        Component posComponent = go.getComponent(POSITION);
        Component drawComponent = go.getComponent(DRAWABLE);
        Component phyComponent = go.getComponent(PHYSICS);
        Component aliveComponent = go.getComponent(ALIVE);
        Component aiComponent = go.getComponent(AI);
        Component lightComponent = go.getComponent(LIGHT);

        if (posComponent != null) posComponents.remove((PositionComponent) posComponent);
        if (drawComponent != null) drawComponents.remove((DrawableComponent) drawComponent);
        if (phyComponent != null) phyComponents.remove((PhysicsComponent) phyComponent);
        if (aliveComponent != null) aliveComponents.remove((AliveComponent) aliveComponent);
        if (aiComponent != null) aiComponents.remove((AIComponent) aiComponent);
        if (lightComponent != null) lightComponents.remove((LightComponent) lightComponent);

        gameWorld.objectsPool.release(go);
        objects.remove(go);
        go.delete();
    }

    public void removeComponent(GameObject go, ComponentType type){
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

    public List<GameObject> getGOByRole(Role role) {
        List<GameObject> gameObjects = new ArrayList<>();
        for (GameObject go : objects) {
            if (go.role == role) {
                gameObjects.add(go);
            }
        }
        return gameObjects;
    }

    public void changeLevel(){
        posComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        phyComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        aliveComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        lightComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        drawComponents.removeIf(component -> ((GameObject) component.getOwner()).role != PLAYER);
        aiComponents.clear();


        for (GameObject gameObject : objects) {
            if (gameObject.role != PLAYER) {
                gameWorld.objectsPool.release(gameObject);
            }
        }
        objects.removeIf(object -> object.role != PLAYER);
    }

    protected void finalize(){
        try {
            super.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        objects.clear();

        posComponents.clear();
        phyComponents.clear();
        aliveComponents.clear();
        lightComponents.clear();
        drawComponents.clear();
        aiComponents.clear();

        instance = null;
    }
}
