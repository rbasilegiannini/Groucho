package com.personal.groucho.game;

import com.personal.groucho.game.animation.Spritesheets;
import com.personal.groucho.game.components.ControllableComponent;
import com.personal.groucho.game.components.PositionComponent;
import com.personal.groucho.game.components.SpriteDrawableComponent;

public class GameObjectFactory {
    public static GameObject makePlayer(int posX, int posY, Controller controller) {
        GameObject gameObject = new GameObject();

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new SpriteDrawableComponent(Spritesheets.groucho_walk));
        gameObject.addComponent(new ControllableComponent(controller));

        return gameObject;
    }
}
