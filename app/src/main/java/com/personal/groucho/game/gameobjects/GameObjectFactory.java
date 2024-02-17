package com.personal.groucho.game.gameobjects;

import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterDimY;
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
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;

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
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.controller.Orientation;
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

import java.util.ArrayList;
import java.util.List;
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
        GameObject gameObject = new GameObject("Groucho", Role.PLAYER);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new SpriteDrawableComponent(grouchoWalk, grouchoDeath));
        gameObject.addComponent(new ControllableComponent(gameworld));
        gameObject.addComponent(new PhysicsComponent(gameworld.getWorld(), characterDimX, characterDimY));
        gameObject.addComponent(new AliveComponent(grouchoHealth));
        gameObject.addComponent(new LightComponent(gameworld.getBuffer()));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 1f, 1f, BodyType.dynamicBody);
        setCharacterPhysics(physics, properties);

        ControllableComponent controllable = (ControllableComponent) gameObject.getComponent(ComponentType.CONTROLLABLE);

        gameworld.controller.addControllerListener(controllable);
        controller.setCurrentState(Idle.getInstance(controller));

        return gameObject;
    }

    public static GameObject makeEnemy(int posX, int posY, Orientation orientation, int health, Spritesheet idle,
                                       StateName originalState, Spritesheet death, GameWorld gameWorld) {
        GameObject gameObject = new GameObject("Enemy", Role.ENEMY);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld(), characterDimX, characterDimY));
        gameObject.addComponent(new SpriteDrawableComponent(idle, death));
        gameObject.addComponent(new AliveComponent(health));
        gameObject.addComponent(new AIComponent(gameWorld, originalState));

        PositionComponent position = (PositionComponent) gameObject.getComponent(ComponentType.POSITION);
        position.setOrientation(orientation);
        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(posX, posY,100f, 1f, BodyType.dynamicBody);
        setCharacterPhysics(physics, properties);

        return gameObject;
    }

    public static GameObject makeWall(int centerX, int centerY, float dimX, float dimY, GameWorld gameWorld) {
        GameObject gameObject = new GameObject("Wall", Role.WALL);

        Paint paint = new Paint();
        paint.setColor(Color.valueOf(188, 143, 143).toArgb());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        gameObject.addComponent(new PositionComponent(centerX, centerY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld(), dimX, dimY));
        gameObject.addComponent(new BoxDrawableComponent(dimX, dimY, paint));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(centerX, centerY, 0f, 0f, BodyType.staticBody);
        setFurniturePhysics(physics, properties);
        setFurnitureOnGameGrid(gameWorld.getGameGrid(), properties, dimX, dimY);

        return gameObject;
    }

    public static List<GameObject> makeHorizontalBorder(int centerX, int centerY,
                                                        float length, GameWorld gameWorld) {
        GameObject roof = new GameObject("Roof", Role.WALL);
        GameObject wall = new GameObject("Wall", Role.WALL);

        Paint paintRoof = new Paint();
        Paint paintWall = new Paint();
        Bitmap wallTexture = Bitmap.createScaledBitmap(Textures.wall,256, 256, false);

        Shader wallShader = new BitmapShader(wallTexture, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paintWall.setShader(wallShader);

        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(Paint.Style.FILL_AND_STROKE);

        int dimX = (int) length;
        int dimRoofY = cellSize;
        int dimWallY = (int) (2*characterScaleFactor*characterDimY);

        wall.addComponent(new PositionComponent(centerX, centerY));
        roof.addComponent(new PositionComponent(centerX, centerY-cellSize));

        wall.addComponent(new PhysicsComponent(gameWorld.getWorld(), dimX-cellSize, dimWallY));
        wall.addComponent(new BoxDrawableComponent(dimX, dimWallY, paintWall));
        roof.addComponent(new BoxDrawableComponent(dimX, dimRoofY, paintRoof));

        PhysicsComponent physics = (PhysicsComponent) wall.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(centerX, centerY, 0f, 0f, BodyType.staticBody);
        setFurniturePhysics(physics, properties);
        setFurnitureOnGameGrid(gameWorld.getGameGrid(), properties, dimX, dimWallY);

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(roof);
        gameObjects.add(wall);

        return gameObjects;
    }

    public static GameObject makeVerticalBorder(int centerX, int centerY, float length, GameWorld gameWorld){
        GameObject border = new GameObject("Wall", Role.WALL);
        Paint paintRoof = new Paint();

        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(Paint.Style.FILL_AND_STROKE);

        int dimY = (int) length;
        int dimX = cellSize/2;

        border.addComponent(new PositionComponent(centerX-cellSize/2, centerY));
        border.addComponent(new PhysicsComponent(gameWorld.getWorld(), dimX, dimY));
        border.addComponent(new BoxDrawableComponent(dimX, dimY, paintRoof));

        PhysicsComponent physics = (PhysicsComponent) border.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(centerX-cellSize/2, centerY, 0f, 0f, BodyType.staticBody);
        setFurniturePhysics(physics, properties);
        setFurnitureOnGameGrid(gameWorld.getGameGrid(), properties, dimX, dimY);

        return border;
    }


    public static GameObject makeFurniture(int centerX, int centerY, float dimX, float dimY, GameWorld gameWorld,
                                           Bitmap texture) {
        GameObject gameObject = new GameObject("Furniture", Role.FURNITURE);

        gameObject.addComponent(new PositionComponent(centerX, centerY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld(), dimX, dimY));
        gameObject.addComponent(new TextureDrawableComponent(texture, (int)dimX, (int)dimY));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(centerX, centerY, 5f, 0, BodyType.dynamicBody);
        setFurniturePhysics(physics, properties);
        setFurnitureOnGameGrid(gameWorld.getGameGrid(), properties, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeHealth(int posX, int posY, GameWorld gameWorld) {
        int dimX = 64;
        int dimY = 64;
        GameObject gameObject = new GameObject("Health", Role.HEALTH);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld(), dimX, dimY));
        gameObject.addComponent(new TextureDrawableComponent(health, dimX, dimY));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 0f, 0, BodyType.staticBody);
        setFurniturePhysics(physics, properties);
        setFurnitureOnGameGrid(gameWorld.getGameGrid(), properties, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeTrigger(String name, int posX, int posY, GameWorld gameWorld) {
        int dimX = 64;
        int dimY = 128;

        GameObject gameObject = new GameObject(name, Role.TRIGGER);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.getWorld(), dimX, dimY));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.PHYSICS);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 0f, 0, BodyType.staticBody);
        setFurniturePhysics(physics, properties);

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
                (characterScaleFactor*toMetersXLength(characterDimX))/2,
                (characterScaleFactor*toMetersYLength(characterDimY))/2,
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

    private static void setFurniturePhysics(PhysicsComponent physics, PhysicsProperties properties) {
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
        box.setAsBox(toMetersXLength(physics.dimX)/2, toMetersYLength(physics.dimY)/2);
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
                cost = 10000000;
                break;
            case dynamicBody:
                cost = 10000*(int)properties.density;
                break;
        }

        for (Node node : nodes) {
            grid.increaseDefaultCostOnNode(node, cost);
        }
    }

}

