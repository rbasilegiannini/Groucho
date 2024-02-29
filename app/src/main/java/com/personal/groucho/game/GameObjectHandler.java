package com.personal.groucho.game;

import static com.personal.groucho.game.gameobjects.Role.PLAYER;

import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Role;

import java.util.ArrayList;
import java.util.List;

public class GameObjectHandler {
    private static GameObjectHandler instance = null;
    private final ComponentHandler compHandler;
    protected final List<GameObject> objects = new ArrayList<>();

    private GameObjectHandler() {
        compHandler = ComponentHandler.getInstance();
    }

    public static GameObjectHandler getInstance() {
        if (instance == null) {
            instance = new GameObjectHandler();
        }
        return instance;
    }

    public void init() {
        objects.clear();
        compHandler.init();
    }

    public void addGameObject(GameObject go) {
        objects.add(go);
        compHandler.addComponents(go);
    }

    public void removeGameObject(GameObject go){
        compHandler.removeAllComponentsFromGO(go);
        Pools.objectsPool.release(go);
        objects.remove(go);
        go.delete();
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
        for (GameObject go : objects) {
            if (go.role != PLAYER) {
                compHandler.removeAllComponentsFromGO(go);
                Pools.objectsPool.release(go);
                go.delete();
            }
        }
        objects.removeIf(object -> object.role != PLAYER);
    }

    public void clear(){
        for (GameObject go : objects) {
            compHandler.removeAllComponentsFromGO(go);
            Pools.objectsPool.release(go);
            go.delete();
        }
        objects.clear();
    }

    protected void finalize(){
        try {
            super.finalize();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        objects.clear();

        instance = null;
    }

}
