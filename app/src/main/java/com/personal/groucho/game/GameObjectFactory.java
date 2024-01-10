package com.personal.groucho.game;

import com.personal.groucho.game.components.PositionComponent;
import com.personal.groucho.game.components.SpriteDrawableComponent;

public class GameObjectFactory {
    public static GameObject makePlayer(int posX, int posY) {
        GameObject gameObject = new GameObject();

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new SpriteDrawableComponent(Spritesheets.groucho_walk));

        return gameObject;
    }
}
