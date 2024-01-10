package com.personal.groucho.game;

import com.personal.groucho.game.components.BoxDrawableComponent;
import com.personal.groucho.game.components.PositionComponent;

public class GameObjectFactory {
    public static GameObject makePlayer(float posX, float posY) {
        GameObject gameObject = new GameObject();

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new BoxDrawableComponent());

        return gameObject;
    }
}
