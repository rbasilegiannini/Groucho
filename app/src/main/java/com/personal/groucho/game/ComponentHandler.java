package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.ComponentType.AI;
import static com.personal.groucho.game.gameobjects.ComponentType.ALIVE;
import static com.personal.groucho.game.gameobjects.ComponentType.CHARACTER;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.LIGHT;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;
import static com.personal.groucho.game.gameobjects.ComponentType.TRIGGER;

import android.util.SparseArray;

import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.BoxDrawableComponent;
import com.personal.groucho.game.gameobjects.components.CharacterComponent;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.DrawableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.gameobjects.components.SpriteComponent;
import com.personal.groucho.game.gameobjects.components.TextureDrawableComponent;
import com.personal.groucho.game.gameobjects.components.TriggerComponent;

import java.util.ArrayList;
import java.util.List;

public class ComponentHandler {
    private static ComponentHandler instance = null;
    private final GameWorld gameWorld;

    protected final List<PositionComponent> posComps = new ArrayList<>();
    protected final List<PhysicsComponent> phyComps = new ArrayList<>();
    protected final ArrayList<DrawableComponent> drawComps = new ArrayList<>();
    protected final List<AIComponent> aiComps = new ArrayList<>();
    protected final List<AliveComponent> aliveComps = new ArrayList<>();
    protected final List<LightComponent> lightComps = new ArrayList<>();
    private final SparseArray<Runnable> releaseStrategy = new SparseArray<>();
    private Component compToRemove;

    private ComponentHandler(GameWorld gameWorld){
        this.gameWorld = gameWorld;

        releaseStrategy.put(POSITION.hashCode(), () -> {
            gameWorld.posCompPool.release((PositionComponent) compToRemove);
            posComps.remove((PositionComponent) compToRemove);
        });
        releaseStrategy.put(PHYSICS.hashCode(), () -> {
            gameWorld.phyCompPool.release((PhysicsComponent)compToRemove);
            phyComps.remove((PhysicsComponent)compToRemove);
        });
        releaseStrategy.put(LIGHT.hashCode(), () -> {
            gameWorld.lightCompPool.release((LightComponent)compToRemove);
            lightComps.remove((LightComponent)compToRemove);
        });
        releaseStrategy.put(ALIVE.hashCode(), () -> {
            gameWorld.aliveCompPool.release((AliveComponent)compToRemove);
            aliveComps.remove((AliveComponent)compToRemove);
        });
        releaseStrategy.put(AI.hashCode(), () -> {
            gameWorld.aiCompPool.release((AIComponent)compToRemove);
            aiComps.remove((AIComponent)compToRemove);
        });
        releaseStrategy.put(TRIGGER.hashCode(), () ->
                gameWorld.triggerCompPool.release((TriggerComponent) compToRemove));
        releaseStrategy.put(CHARACTER.hashCode(), () ->
                gameWorld.charCompPool.release((CharacterComponent) compToRemove));
        releaseStrategy.put(CONTROLLABLE.hashCode(), () ->
                gameWorld.ctrlCompPool.release((ControllableComponent) compToRemove));
        releaseStrategy.put(DRAWABLE.hashCode(), this::removeDrawableComp);
    }

    public static ComponentHandler getInstance(GameWorld gameWorld){
        if (instance == null) {
            instance = new ComponentHandler(gameWorld);
        }
        return instance;
    }

    private void removeDrawableComp() {
        if (compToRemove.getClass().equals(BoxDrawableComponent.class)) {
            gameWorld.boxCompPool.release((BoxDrawableComponent) compToRemove);
        }
        else if (compToRemove.getClass().equals(SpriteComponent.class)) {
            gameWorld.spriteCompPool.release((SpriteComponent) compToRemove);
        }
        else if (compToRemove.getClass().equals(TextureDrawableComponent.class)) {
            gameWorld.textureCompPool.release((TextureDrawableComponent) compToRemove);
        }
        drawComps.remove((DrawableComponent)compToRemove);
    }

    public void init() {
        phyComps.clear();
        aliveComps.clear();
        lightComps.clear();
        drawComps.clear();
        aiComps.clear();
        posComps.clear();
    }

    public void addComponents(GameObject go) {
        Component posComponent = go.getComponent(POSITION);
        Component drawComponent = go.getComponent(DRAWABLE);
        Component phyComponent = go.getComponent(PHYSICS);
        Component aliveComponent = go.getComponent(ALIVE);
        Component aiComponent = go.getComponent(AI);
        Component lightComponent = go.getComponent(LIGHT);

        if (posComponent != null) posComps.add((PositionComponent) posComponent);
        if (drawComponent != null) drawComps.add((DrawableComponent) drawComponent);
        if (phyComponent != null) phyComps.add((PhysicsComponent) phyComponent);
        if (aliveComponent != null) aliveComps.add((AliveComponent) aliveComponent);
        if (aiComponent != null) aiComps.add((AIComponent) aiComponent);
        if (lightComponent != null) lightComps.add((LightComponent) lightComponent);
    }

    public void removeAllComponentsFromGO(GameObject go) {
        for (ComponentType type : ComponentType.getAllComponentTypes()) {
            Component component = go.getComponent(type);
            if (component != null) {
                removeComponent(go, type);
            }
        }
    }

    public void removeComponent(GameObject go, ComponentType type){
        compToRemove = go.getComponent(type);
        if (compToRemove != null) {
            releaseStrategy.get(type.hashCode()).run();
            go.removeComponent(type);
        }
    }

    protected void finalize(){
        posComps.clear();
        phyComps.clear();
        aliveComps.clear();
        lightComps.clear();
        drawComps.clear();
        aiComps.clear();

        instance = null;
    }
}
