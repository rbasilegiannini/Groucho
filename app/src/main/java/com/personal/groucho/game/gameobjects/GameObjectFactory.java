package com.personal.groucho.game.gameobjects;

import static com.personal.groucho.game.constants.System.characterDimensionsX;
import static com.personal.groucho.game.constants.System.characterDimensionsY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;
import static com.personal.groucho.game.constants.CharacterProperties.grouchoHealth;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.toMetersXLength;
import static com.personal.groucho.game.Utils.toMetersYLength;
import static com.personal.groucho.game.assets.Spritesheets.grouchoDeath;
import static com.personal.groucho.game.assets.Spritesheets.grouchoWalk;
import static com.personal.groucho.game.assets.Textures.health;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.AI.states.StateName;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.BoxDrawableComponent;
import com.personal.groucho.game.gameobjects.components.ControllableComponent;
import com.personal.groucho.game.gameobjects.components.LightComponent;
import com.personal.groucho.game.gameobjects.components.PhysicsComponent;
import com.personal.groucho.game.gameobjects.components.PositionComponent;
import com.personal.groucho.game.gameobjects.components.SpriteDrawableComponent;
import com.personal.groucho.game.gameobjects.components.TextureDrawableComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.states.Idle;

import java.util.Set;

public class GameObjectFactory {

    private static class PhysicsProperties {
        int positionX;
        int positionY;
        float density;
        float friction;
        BodyType type;

        PhysicsProperties(int positionX, int positionY, float density, float friction, BodyType type) {
            this.positionX = positionX;
            this.positionY = positionY;
            this.density = density;
            this.friction = friction;
            this.type = type;
        }
    }

    public static GameObject makePlayer(int posX, int posY, Controller controller, GameWorld gameworld) {
        GameObject gameObject = new GameObject("Groucho", Role.PLAYER, gameworld);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new SpriteDrawableComponent(grouchoWalk, grouchoDeath));
        gameObject.addComponent(new ControllableComponent(controller, gameworld));
        gameObject.addComponent(new PhysicsComponent(gameworld.getWorld()));
        gameObject.addComponent(new AliveComponent(grouchoHealth));
        gameObject.addComponent(new LightComponent(gameworld));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 1f, 1f, BodyType.dynamicBody);
        setCharacterPhysics(physics, properties);

        ControllableComponent controllable = (ControllableComponent) gameObject.getComponent(ComponentType.CONTROLLABLE);

        gameworld.controller.addControllerListener(controllable);
        controller.setCurrentState(Idle.getInstance(controller));

        return gameObject;
    }

    public static GameObject makeEnemy(int posX, int posY, int health, Spritesheet idle,
                                       StateName originalState, Spritesheet death, GameWorld gameWorld) {
        GameObject gameObject = new GameObject("Enemy", Role.ENEMY, gameWorld);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld()));
        gameObject.addComponent(new SpriteDrawableComponent(idle, death));
        gameObject.addComponent(new AliveComponent(health));
        gameObject.addComponent(new AIComponent(gameWorld, originalState));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(posX, posY,100f, 1f, BodyType.kinematicBody);
        setCharacterPhysics(physics, properties);

        return gameObject;
    }

    public static GameObject makeWall(int centerX, int centerY, float dimX, float dimY, GameWorld gameWorld) {
        GameObject gameObject = new GameObject("Wall", Role.WALL, gameWorld);

        Paint paint = new Paint();
        paint.setColor(Color.valueOf(188, 143, 143).toArgb());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        gameObject.addComponent(new PositionComponent(centerX, centerY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld()));
        gameObject.addComponent(new BoxDrawableComponent(dimX, dimY, paint));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(centerX, centerY, 0f, 0f, BodyType.staticBody);
        setFurniturePhysics(physics, properties, dimX, dimY);
        setFurnitureOnGameGrid(gameWorld.getGameGrid(), properties, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeFurniture(int centerX, int centerY, float dimX, float dimY, GameWorld gameWorld,
                                           Bitmap texture) {
        GameObject gameObject = new GameObject("Furniture", Role.FURNITURE, gameWorld);

        gameObject.addComponent(new PositionComponent(centerX, centerY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld()));
        gameObject.addComponent(new TextureDrawableComponent(texture, (int)dimX, (int)dimY));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(centerX, centerY, 5f, 0, BodyType.dynamicBody);
        setFurniturePhysics(physics, properties, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeHealth(int posX, int posY, GameWorld gameWorld) {
        int dimX = 64;
        int dimY = 64;
        GameObject gameObject = new GameObject("Health", Role.HEALTH, gameWorld);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld()));
        gameObject.addComponent(new TextureDrawableComponent(health, dimX, dimY));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 0f, 0, BodyType.staticBody);
        setFurniturePhysics(physics, properties, dimX, dimY);

        return gameObject;
    }

    private static void setCharacterPhysics(PhysicsComponent physics, PhysicsProperties properties) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setLinearDamping(10f);
        bodyDef.setPosition(new Vec2(
                fromBufferToMetersX(properties.positionX),
                fromBufferToMetersY(properties.positionY))
        );
        bodyDef.setType(properties.type);
        bodyDef.setAllowSleep(false);
        physics.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(
                (characterScaleFactor*toMetersXLength(characterDimensionsX))/2,
                (characterScaleFactor*toMetersYLength(characterDimensionsY))/2,
                0,-0.5f,0
        );

        fixtureDef.setShape(box);
        fixtureDef.setDensity(properties.density);
        fixtureDef.setFriction(properties.friction);
        physics.addFixture(fixtureDef);

        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }


    private static void setFurniturePhysics(PhysicsComponent physics, PhysicsProperties properties,
                                            float dimX, float dimY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setLinearDamping(1f);

        bodyDef.setPosition(new Vec2(
                fromBufferToMetersX(properties.positionX),
                fromBufferToMetersY(properties.positionY))
        );
        bodyDef.setType(properties.type);
        physics.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(toMetersXLength(dimX)/2, toMetersYLength(dimY)/2);
        fixtureDef.setShape(box);
        fixtureDef.setFriction(properties.friction);
        fixtureDef.setDensity(properties.density);
        physics.addFixture(fixtureDef);
        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }

    private static void setFurnitureOnGameGrid(GameGrid grid, PhysicsProperties properties, float dimX,
                                               float dimY) {
        Set<Node> nodes = grid.getNodes(properties.positionX, properties.positionY, (int)dimX, (int)dimY);

        int cost = 0;
        switch (properties.type){
            case staticBody:
                cost = 100000;
                break;
            case dynamicBody:
                cost = (int)properties.density;
                break;
        }

        for (Node node : nodes) {
            grid.setDefaultCostOnNode(node, cost);
        }
    }

}

