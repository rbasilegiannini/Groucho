package com.personal.groucho.game;

import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.collisions.Collision;
import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.components.AIComponent;
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

public class Pools {
    public static final ObjectsPool<GameObject> objectsPool =
            new ObjectsPool<>(100, GameObject.class);
    public static final ObjectsPool<Collision> collisionsPool =
            new ObjectsPool<>(30, Collision.class);
    public static final ObjectsPool<Node> nodesPool =
            new ObjectsPool<>(200, Node.class);
    public static final ObjectsPool<PositionComponent> posCompPool =
            new ObjectsPool<>(100, PositionComponent.class);
    public static final ObjectsPool<PhysicsComponent> phyCompPool =
            new ObjectsPool<>(100, PhysicsComponent.class);
    public static final ObjectsPool<AIComponent> aiCompPool =
            new ObjectsPool<>(10, AIComponent.class);
    public static final ObjectsPool<SpriteComponent> spriteCompPool =
            new ObjectsPool<>(100, SpriteComponent.class);
    public static final ObjectsPool<TextureDrawableComponent> textureCompPool =
            new ObjectsPool<>(100, TextureDrawableComponent.class);
    public static final ObjectsPool<BoxDrawableComponent> boxCompPool =
            new ObjectsPool<>(100, BoxDrawableComponent.class);
    public static final ObjectsPool<AliveComponent> aliveCompPool =
            new ObjectsPool<>(100, AliveComponent.class);
    public static final ObjectsPool<CharacterComponent> charCompPool =
            new ObjectsPool<>(100, CharacterComponent.class);
    public static final ObjectsPool<LightComponent> lightCompPool =
            new ObjectsPool<>(1, LightComponent.class);
    public static final ObjectsPool<ControllableComponent> ctrlCompPool =
            new ObjectsPool<>(1, ControllableComponent.class);
    public static final ObjectsPool<TriggerComponent> triggerCompPool =
            new ObjectsPool<>(30, TriggerComponent.class);
    public static final ObjectsPool<TextBlock> textBlocksPool =
            new ObjectsPool<>(20, TextBlock.class);
}
