package com.personal.groucho.game.gameobjects;

import static android.graphics.Paint.Style.FILL_AND_STROKE;
import static android.graphics.Shader.TileMode.REPEAT;
import static com.google.fpl.liquidfun.BodyType.dynamicBody;
import static com.google.fpl.liquidfun.BodyType.staticBody;
import static com.personal.groucho.game.CharacterFactory.getGroucho;
import static com.personal.groucho.game.CharacterFactory.getSkeleton;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterDimY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.toMetersXLength;
import static com.personal.groucho.game.Utils.toMetersYLength;
import static com.personal.groucho.game.assets.Spritesheets.grouchoWalk;
import static com.personal.groucho.game.assets.Textures.health;
import static com.personal.groucho.game.gameobjects.ComponentType.CONTROLLABLE;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.Role.ENEMY;
import static com.personal.groucho.game.gameobjects.Role.FURNITURE;
import static com.personal.groucho.game.gameobjects.Role.HEALTH;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;
import static com.personal.groucho.game.gameobjects.Role.TRIGGER;
import static com.personal.groucho.game.gameobjects.Role.WALL;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.SparseArray;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.controller.Orientation;
import com.personal.groucho.game.controller.states.StateName;
import com.personal.groucho.game.gameobjects.components.AIComponent;
import com.personal.groucho.game.gameobjects.components.AliveComponent;
import com.personal.groucho.game.gameobjects.components.BoxDrawableComponent;
import com.personal.groucho.game.gameobjects.components.CharacterComponent;
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

public class GameObjectFactory {

    private static class PhysicsProp {
        int posX;
        int posY;
        float phyCenterX;
        float phyCenterY;
        float density;
        float friction;
        BodyType type;

        PhysicsProp(int positionX, int positionY, float phyCenterX, float phyCenterY,
                    float density, float friction, BodyType type) {
            this.posX = positionX;
            this.posY = positionY;
            this.phyCenterX = phyCenterX;
            this.phyCenterY = phyCenterY;
            this.density = density;
            this.friction = friction;
            this.type = type;
        }
    }

    public static GameObject makePlayer(int posX, int posY, Controller controller, GameWorld gameWorld) {
        GameObject gameObject = gameWorld.objectsPool.acquire();
        gameObject.init("Player", PLAYER);


        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new SpriteDrawableComponent(grouchoWalk));
        gameObject.addComponent(new ControllableComponent(gameWorld));
        gameObject.addComponent(new PhysicsComponent(gameWorld.physics.world,
                characterScaleFactor*characterDimX, characterScaleFactor*characterDimY));
        gameObject.addComponent(new AliveComponent(gameWorld));
        gameObject.addComponent(new CharacterComponent(getGroucho()));
        gameObject.addComponent(new LightComponent(gameWorld.graphics.buffer));

        PhysicsComponent phyComp = (PhysicsComponent) gameObject.getComponent(PHYSICS);
        PhysicsProp properties = new PhysicsProp(posX, posY, 0f, -1f,
                1f, 1f, dynamicBody);
        setCharacterPhysics(phyComp, properties);

        ControllableComponent ctrlComp = (ControllableComponent) gameObject.getComponent(CONTROLLABLE);

        gameWorld.controller.addControllerListener(ctrlComp);
        controller.setCurrentState(Idle.getInstance(controller));

        return gameObject;
    }

    public static GameObject makeEnemy(
            int posX, int posY, Orientation orientation, Spritesheet idle,
            StateName state, GameWorld gameWorld) {

        GameObject gameObject = gameWorld.objectsPool.acquire();
        gameObject.init("Enemy", ENEMY);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.physics.world,
                characterScaleFactor*characterDimX, characterScaleFactor*characterDimY));
        gameObject.addComponent(new SpriteDrawableComponent(idle));
        gameObject.addComponent(new AliveComponent(gameWorld));
        gameObject.addComponent(new CharacterComponent(getSkeleton()));
        gameObject.addComponent(new AIComponent(gameWorld, state));

        PositionComponent posComp = (PositionComponent) gameObject.getComponent(ComponentType.POSITION);
        posComp.setOrientation(orientation);
        PhysicsComponent phyComp = (PhysicsComponent) gameObject.getComponent(PHYSICS);
        PhysicsProp prop = new PhysicsProp(posX, posY,0f, -1f,
                100f, 1f, dynamicBody);
        setCharacterPhysics(phyComp, prop);

        return gameObject;
    }

    public static List<GameObject> makeWall(int centerX, int centerY, float length, GameWorld gameWorld) {
        GameObject roof = gameWorld.objectsPool.acquire();
        GameObject wall = gameWorld.objectsPool.acquire();

        roof.init("Roof", WALL);
        wall.init("Wall", WALL);

        Paint paintRoof = new Paint();
        Paint paintWall = new Paint();
        Bitmap wallTexture = Bitmap.createScaledBitmap(Textures.wall,256, 256, false);

        Shader wallShader = new BitmapShader(wallTexture, REPEAT, REPEAT);
        paintWall.setShader(wallShader);
        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(FILL_AND_STROKE);

        int dimX = cellSize/2;
        int dimWallY = (int) (2*characterScaleFactor*characterDimY);
        int dimRoofY = (int) (length - dimWallY);

        roof.addComponent(new PositionComponent(centerX, centerY));
        wall.addComponent(new PositionComponent(centerX, centerY+dimRoofY/2+dimWallY/2));
        wall.addComponent(new BoxDrawableComponent(dimX, dimWallY, paintWall));
        roof.addComponent(new BoxDrawableComponent(dimX, dimRoofY, paintRoof));
        roof.addComponent(new PhysicsComponent(gameWorld.physics.world, dimX, length-dimWallY));

        PhysicsComponent phyComp = (PhysicsComponent) roof.getComponent(PHYSICS);
        PhysicsProp prop = new PhysicsProp(
                centerX,
                centerY + dimWallY/2,
                0f,
                0,
                0f, 0f, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(gameWorld.grid, prop, dimX, length);

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(roof);
        gameObjects.add(wall);

        return gameObjects;
    }

    public static List<GameObject> makeHorBorder(int centerX, int centerY, float length, GameWorld gameWorld) {
        GameObject roof = gameWorld.objectsPool.acquire();
        GameObject wall = gameWorld.objectsPool.acquire();

        roof.init("Roof", WALL);
        wall.init("Wall", WALL);

        Paint paintRoof = new Paint();
        Paint paintWall = new Paint();
        Bitmap wallTexture = Bitmap.createScaledBitmap(Textures.wall,256, 256, false);

        Shader wallShader = new BitmapShader(wallTexture, REPEAT, REPEAT);
        paintWall.setShader(wallShader);

        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(FILL_AND_STROKE);

        int dimX = (int) length;
        int dimRoofY = cellSize;
        int dimWallY = (int) (2*characterScaleFactor*characterDimY);

        wall.addComponent(new PositionComponent(centerX, centerY));
        roof.addComponent(new PositionComponent(centerX, centerY-cellSize));

        wall.addComponent(new PhysicsComponent(gameWorld.physics.world, dimX, dimWallY));
        wall.addComponent(new BoxDrawableComponent(dimX, dimWallY, paintWall));
        roof.addComponent(new BoxDrawableComponent(dimX, dimRoofY, paintRoof));

        PhysicsComponent phyComp = (PhysicsComponent) wall.getComponent(PHYSICS);
        PhysicsProp prop = new PhysicsProp(centerX, centerY, 0f, 0f,
                0f, 0f, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(gameWorld.grid, prop, dimX, dimWallY);

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(roof);
        gameObjects.add(wall);

        return gameObjects;
    }

    public static GameObject makeVerBorder(int centerX, int centerY, float length, GameWorld gameWorld){
        GameObject border = gameWorld.objectsPool.acquire();
        border.init("Wall", WALL);

        Paint paintRoof = new Paint();

        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(FILL_AND_STROKE);

        int dimY = (int) length;
        int dimX = cellSize/2;

        border.addComponent(new PositionComponent(centerX-cellSize/2, centerY));
        border.addComponent(new PhysicsComponent(gameWorld.physics.world, dimX, dimY));
        border.addComponent(new BoxDrawableComponent(dimX, dimY, paintRoof));

        PhysicsComponent phyComp = (PhysicsComponent) border.getComponent(PHYSICS);
        PhysicsProp prop = new PhysicsProp(
                centerX-cellSize/2, centerY, 0f, 0f,
                0f, 0f, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(gameWorld.grid, prop, dimX, dimY);

        return border;
    }


    public static GameObject makeFurniture(int centerX, int centerY, float dimX, float dimY, GameWorld gameWorld,
                                           Bitmap texture) {
        GameObject gameObject = gameWorld.objectsPool.acquire();
        gameObject.init("Furniture", FURNITURE);

        gameObject.addComponent(new PositionComponent(centerX, centerY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.physics.world, dimX, dimY/2));
        gameObject.addComponent(new TextureDrawableComponent(texture, (int)dimX, (int)dimY));

        PhysicsComponent phyComp = (PhysicsComponent) gameObject.getComponent(PHYSICS);
        PhysicsProp prop = new PhysicsProp(centerX, centerY, 0f,0f,
                5f, 0, dynamicBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(gameWorld.grid, prop, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeHealth(int posX, int posY, GameWorld gameWorld) {
        int dimX = 64;
        int dimY = 64;
        GameObject gameObject = gameWorld.objectsPool.acquire();
        gameObject.init("Health", HEALTH);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.physics.world, dimX, dimY));
        gameObject.addComponent(new TextureDrawableComponent(health, dimX, dimY));

        PhysicsComponent phyComp = (PhysicsComponent) gameObject.getComponent(PHYSICS);
        PhysicsProp prop = new PhysicsProp(posX, posY, 0f, 0f,
                0f, 0, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(gameWorld.grid, prop, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeTrigger(String name, int posX, int posY, GameWorld gameWorld) {
        int dimX = 64;
        int dimY = 128;

        GameObject gameObject = gameWorld.objectsPool.acquire();
        gameObject.init(name, TRIGGER);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(gameWorld.physics.world, dimX, dimY));

        PhysicsComponent phyComp = (PhysicsComponent) gameObject.getComponent(PHYSICS);
        PhysicsProp prop = new PhysicsProp(posX, posY, 0f, 0f,
                0f, 0, staticBody);
        setFurniturePhysics(phyComp, prop);

        return gameObject;
    }

    private static void setCharacterPhysics(PhysicsComponent physics, PhysicsProp prop) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setLinearDamping(10f);
        bodyDef.setPosition(new Vec2(
                fromBufferToMetersX(prop.posX),
                fromBufferToMetersY(prop.posY))
        );
        bodyDef.setType(prop.type);
        bodyDef.setAllowSleep(false);
        physics.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(
                toMetersXLength(physics.dimX)/2,
                toMetersYLength(physics.dimY)/2,
                prop.phyCenterX,
                prop.phyCenterY
                ,0);
        fixtureDef.setShape(box);
        fixtureDef.setDensity(prop.density);
        fixtureDef.setFriction(prop.friction);
        physics.addFixture(fixtureDef);

        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }

    private static void setFurniturePhysics(PhysicsComponent physics, PhysicsProp prop) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setLinearDamping(1f);

        bodyDef.setPosition(new Vec2(
                fromBufferToMetersX(prop.posX),
                fromBufferToMetersY(prop.posY))
        );
        bodyDef.setType(prop.type);
        physics.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(
                toMetersXLength(physics.dimX)/2,
                toMetersYLength(physics.dimY)/2,
                prop.phyCenterX,
                prop.phyCenterY,
                0);
        fixtureDef.setShape(box);
        fixtureDef.setFriction(prop.friction);
        fixtureDef.setDensity(prop.density);
        physics.addFixture(fixtureDef);
        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }

    private static void setFurnitureOnGameGrid(GameGrid grid, PhysicsProp prop, float dimX, float dimY) {
        SparseArray<Node>  nodes= grid.getNodes(prop.posX, prop.posY, (int)dimX, (int)dimY);

        int cost = 0;
        switch (prop.type){
            case staticBody:
                cost = 10000000;
                break;
            case dynamicBody:
                cost = 10000*(int)prop.density;
                break;
        }

        for (int i = 0; i < nodes.size(); i++) {
            grid.increaseDefaultCostOnNode(nodes.valueAt(i), cost);
        }
    }

}

