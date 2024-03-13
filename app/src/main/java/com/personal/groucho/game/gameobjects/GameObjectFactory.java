package com.personal.groucho.game.gameobjects;

import static android.graphics.Paint.Style.FILL_AND_STROKE;
import static android.graphics.Shader.TileMode.REPEAT;
import static com.google.fpl.liquidfun.BodyType.dynamicBody;
import static com.google.fpl.liquidfun.BodyType.kinematicBody;
import static com.google.fpl.liquidfun.BodyType.staticBody;
import static com.personal.groucho.game.CharacterFactory.getGroucho;
import static com.personal.groucho.game.assets.Spritesheets.grouchoIdle;
import static com.personal.groucho.game.constants.System.cellSize;
import static com.personal.groucho.game.constants.System.charDimX;
import static com.personal.groucho.game.constants.System.charDimY;
import static com.personal.groucho.game.constants.System.charScaleFactor;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.toMetersXLength;
import static com.personal.groucho.game.Utils.toMetersYLength;
import static com.personal.groucho.game.assets.Textures.health;
import static com.personal.groucho.game.gameobjects.Role.ENEMY;
import static com.personal.groucho.game.gameobjects.Role.FLOOR;
import static com.personal.groucho.game.gameobjects.Role.FURNITURE;
import static com.personal.groucho.game.gameobjects.Role.HEALTH;
import static com.personal.groucho.game.gameobjects.Role.NEUTRAL;
import static com.personal.groucho.game.gameobjects.Role.PLAYER;
import static com.personal.groucho.game.gameobjects.Role.TOP;
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
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.AI.pathfinding.GameGrid;
import com.personal.groucho.game.AI.pathfinding.Node;
import com.personal.groucho.game.CharacterFactory;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Pools;
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
import com.personal.groucho.game.gameobjects.components.SpriteComponent;
import com.personal.groucho.game.gameobjects.components.TextureComponent;
import com.personal.groucho.game.controller.Controller;
import com.personal.groucho.game.controller.states.Idle;
import com.personal.groucho.game.gameobjects.components.TriggerComponent;

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
        GameObject gameObject = Pools.objectsPool.acquire();

        PositionComponent posComp = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        SpriteComponent spriteComp = Pools.spriteCompPool.acquire();
        ControllableComponent ctrlComp = Pools.ctrlCompPool.acquire();
        AliveComponent aliveComp = Pools.aliveCompPool.acquire();
        CharacterComponent charComp = Pools.charCompPool.acquire();
        LightComponent lightComp = Pools.lightCompPool.acquire();

        gameObject.init("Player", PLAYER);
        posComp.init(posX, posY);
        phyComp.init(gameWorld.physics.world, charScaleFactor * charDimX, charScaleFactor * charDimY);
        spriteComp.init(grouchoIdle);
        ctrlComp.init(gameWorld);
        charComp.init(getGroucho());
        aliveComp.init(gameWorld, charComp.properties.health);
        lightComp.init(gameWorld.graphics.buffer);

        gameObject.addComponent(posComp);
        gameObject.addComponent(phyComp);
        gameObject.addComponent(spriteComp);
        gameObject.addComponent(ctrlComp);
        gameObject.addComponent(aliveComp);
        gameObject.addComponent(charComp);
        gameObject.addComponent(lightComp);

        PhysicsProp properties = new PhysicsProp(posX, posY, 0f, -1f,
                1f, 1f, dynamicBody);
        setCharacterPhysics(phyComp, properties);

        gameWorld.controller.addControllerListener(ctrlComp);
        controller.setCurrentState(Idle.getInstance(controller));

        return gameObject;
    }

    public static GameObject makeEnemy(
            int posX, int posY, Orientation orientation, CharacterFactory.CharacterProp charProp,
            StateName state, GameWorld gameWorld) {

        GameObject gameObject = Pools.objectsPool.acquire();

        PositionComponent posComp = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        SpriteComponent spriteComp = Pools.spriteCompPool.acquire();
        AIComponent aiComp = Pools.aiCompPool.acquire();
        AliveComponent aliveComp = Pools.aliveCompPool.acquire();
        CharacterComponent charComp = Pools.charCompPool.acquire();

        gameObject.init("Enemy", ENEMY);

        posComp.init(posX, posY, orientation);
        phyComp.init(gameWorld.physics.world, charScaleFactor * charDimX, charScaleFactor * charDimY);
        spriteComp.init(charProp.sheetIdle);
        aiComp.init(gameWorld, state);
        charComp.init(charProp);
        aliveComp.init(gameWorld, charComp.properties.health);

        gameObject.addComponent(posComp);

        gameObject.addComponent(posComp);
        gameObject.addComponent(phyComp);
        gameObject.addComponent(spriteComp);
        gameObject.addComponent(aiComp);
        gameObject.addComponent(aliveComp);
        gameObject.addComponent(charComp);

        PhysicsProp prop = new PhysicsProp(posX, posY,0f, -1f,
                25f, 1f, kinematicBody);
        setCharacterPhysics(phyComp, prop);

        return gameObject;
    }

    public static List<GameObject> makeWall(int centerX, int centerY, float length, World world) {
        int dimX = cellSize/2;
        int dimWallY = (int) (2* charScaleFactor * charDimY);
        int dimRoofY = (int) (length - dimWallY);
        Paint paintRoof = new Paint();
        Paint paintWall = new Paint();
        Bitmap wallTexture = Bitmap.createScaledBitmap(Textures.whiteWall,256, 256, false);

        Shader wallShader = new BitmapShader(wallTexture, REPEAT, REPEAT);
        paintWall.setShader(wallShader);
        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(FILL_AND_STROKE);

        GameObject roof = Pools.objectsPool.acquire();
        GameObject wall = Pools.objectsPool.acquire();

        PositionComponent posCompRoof = Pools.posCompPool.acquire();
        PositionComponent posCompWall = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        BoxDrawableComponent boxCompWall = Pools.boxCompPool.acquire();
        BoxDrawableComponent boxCompRoof = Pools.boxCompPool.acquire();

        roof.init("Roof", WALL);
        wall.init("Wall", WALL);
        posCompRoof.init(centerX, centerY);
        posCompWall.init(centerX, centerY+dimRoofY/2+dimWallY/2);
        phyComp.init(world, dimX, length-dimWallY);
        boxCompWall.init(dimX, dimWallY, paintWall);
        boxCompRoof.init(dimX, dimRoofY, paintRoof);

        roof.addComponent(posCompRoof);
        wall.addComponent(posCompWall);
        wall.addComponent(boxCompWall);
        roof.addComponent(boxCompRoof);
        roof.addComponent(phyComp);

        PhysicsProp prop = new PhysicsProp(
                centerX,
                centerY + dimWallY/2,
                0f,
                0,
                0f, 0f, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(prop, dimX, length);

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(roof);
        gameObjects.add(wall);

        return gameObjects;
    }

    public static List<GameObject> makeHorBorder(int centerX, int centerY, float length, Bitmap texture,
                                                 World world) {

        int dimX = (int) length;
        int dimRoofY = cellSize/2;
        int dimWallY = (int) (2* charScaleFactor * charDimY);

        Paint paintRoof = new Paint();
        Paint paintWall = new Paint();
        Bitmap wallTexture = Bitmap.createScaledBitmap(texture,256, dimWallY, false);

        Shader wallShader = new BitmapShader(wallTexture, REPEAT, REPEAT);
        paintWall.setShader(wallShader);

        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(FILL_AND_STROKE);

        GameObject roof = Pools.objectsPool.acquire();
        GameObject wall = Pools.objectsPool.acquire();

        PositionComponent posCompRoof = Pools.posCompPool.acquire();
        PositionComponent posCompWall = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        BoxDrawableComponent boxCompWall = Pools.boxCompPool.acquire();
        BoxDrawableComponent boxCompRoof = Pools.boxCompPool.acquire();

        roof.init("Roof", TOP);
        wall.init("Wall", WALL);

        posCompWall.init(centerX, centerY);
        posCompRoof.init(centerX, centerY-cellSize);
        phyComp.init(world, dimX, dimWallY);
        boxCompWall.init(dimX, dimWallY, paintWall);
        boxCompRoof.init(dimX, dimRoofY, paintRoof);

        wall.addComponent(posCompWall);
        roof.addComponent(posCompRoof);
        wall.addComponent(phyComp);
        wall.addComponent(boxCompWall);
        roof.addComponent(boxCompRoof);

        PhysicsProp prop = new PhysicsProp(centerX, centerY, 0f, 0f,
                0f, 0f, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(prop, dimX, dimWallY);

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.add(roof);
        gameObjects.add(wall);

        return gameObjects;
    }

    public static GameObject makeVerBorder(int centerX, int centerY, float length, World world){
        int dimY = (int) length;
        int dimX = cellSize/2;
        Paint paintRoof = new Paint();
        paintRoof.setColor(Color.argb(255, 92,64,51));
        paintRoof.setStyle(FILL_AND_STROKE);

        GameObject border = Pools.objectsPool.acquire();

        PositionComponent posComp = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        BoxDrawableComponent boxComp = Pools.boxCompPool.acquire();

        border.init("Wall", TOP);

        posComp.init(centerX-cellSize/2, centerY);
        phyComp.init(world, (float) (dimX+0.2*cellSize), dimY);
        boxComp.init(dimX, dimY, paintRoof);

        border.addComponent(posComp);
        border.addComponent(phyComp);
        border.addComponent(boxComp);

        PhysicsProp prop = new PhysicsProp(
                centerX-cellSize/2, centerY, 0f, 0f,
                0f, 0f, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(prop, dimX, dimY);

        return border;
    }

    public static GameObject makeDynamicFurniture(int centerX, int centerY, float dimX, float dimY, float density,
                                                  World world, Bitmap texture) {
        GameObject gameObject = Pools.objectsPool.acquire();

        PositionComponent posComp = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        TextureComponent textureComp = Pools.textureCompPool.acquire();

        gameObject.init("Furniture", FURNITURE);

        posComp.init(centerX, centerY);
        phyComp.init(world, dimX, dimY/2);
        textureComp.init(texture, (int)dimX, (int)dimY);

        gameObject.addComponent(posComp);
        gameObject.addComponent(phyComp);
        gameObject.addComponent(textureComp);

        PhysicsProp prop = new PhysicsProp(centerX, centerY, 0f,0f,
                density, 0, dynamicBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(prop, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeStaticFurniture(int centerX, int centerY, float dimX, float dimY,
                                                  World world, Bitmap texture) {
        GameObject gameObject = Pools.objectsPool.acquire();

        PositionComponent posComp = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        TextureComponent textureComp = Pools.textureCompPool.acquire();

        gameObject.init("Furniture", FURNITURE);

        posComp.init(centerX, centerY);
        phyComp.init(world, dimX, dimY/4);
        textureComp.init(texture, (int)dimX, (int)dimY);

        gameObject.addComponent(posComp);
        gameObject.addComponent(phyComp);
        gameObject.addComponent(textureComp);

        PhysicsProp prop = new PhysicsProp(centerX, centerY, 0f,0.5f,
                0f, 0, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(prop, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeWallDecoration(int centerX, int centerY, float dimX, float dimY,
                                                Bitmap texture) {
        GameObject gameObject = Pools.objectsPool.acquire();
        PositionComponent posComp = Pools.posCompPool.acquire();
        TextureComponent textureComp = Pools.textureCompPool.acquire();

        gameObject.init("Decoration", NEUTRAL);

        posComp.init(centerX, centerY);
        textureComp.init(texture, (int)dimX, (int)dimY);

        gameObject.addComponent(posComp);
        gameObject.addComponent(textureComp);

        return gameObject;
    }

    public static GameObject makeFloorDecoration(int centerX, int centerY, float dimX, float dimY,
                                                 Bitmap texture) {
        GameObject gameObject = Pools.objectsPool.acquire();
        PositionComponent posComp = Pools.posCompPool.acquire();
        TextureComponent textureComp = Pools.textureCompPool.acquire();

        gameObject.init("Decoration", FLOOR);

        posComp.init(centerX, centerY);
        textureComp.init(texture, (int)dimX, (int)dimY);

        gameObject.addComponent(posComp);
        gameObject.addComponent(textureComp);

        return gameObject;
    }

    public static GameObject makeHealth(int posX, int posY, World world) {
        int dimX = 64;
        int dimY = 64;
        GameObject gameObject = Pools.objectsPool.acquire();
        PositionComponent posComp = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        TextureComponent textureComp = Pools.textureCompPool.acquire();

        gameObject.init("Health", HEALTH);
        posComp.init(posX, posY);
        phyComp.init(world, dimX, dimY);
        textureComp.init(health, dimX, dimY);

        gameObject.addComponent(posComp);
        gameObject.addComponent(phyComp);
        gameObject.addComponent(textureComp);

        PhysicsProp prop = new PhysicsProp(posX, posY, 0f, 0f,
                0f, 0, staticBody);
        setFurniturePhysics(phyComp, prop);
        setFurnitureOnGameGrid(prop, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeTrigger(int posX, int posY, int dimX, int dimY, World world,
                                         Runnable runnable) {

        GameObject gameObject = Pools.objectsPool.acquire();
        PositionComponent posComp = Pools.posCompPool.acquire();
        PhysicsComponent phyComp = Pools.phyCompPool.acquire();
        TriggerComponent triggerComp = Pools.triggerCompPool.acquire();

        gameObject.init("Trigger", TRIGGER);

        posComp.init(posX, posY);
        phyComp.init(world, dimX, dimY);
        triggerComp.init(runnable);

        gameObject.addComponent(posComp);
        gameObject.addComponent(phyComp);
        gameObject.addComponent(triggerComp);

        PhysicsProp prop = new PhysicsProp(posX, posY, 0f, 0f,
                0f, 0, staticBody);
        setFurniturePhysics(phyComp, prop);

        return gameObject;
    }

    private static void setCharacterPhysics(PhysicsComponent phyComp, PhysicsProp prop) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setLinearDamping(10f);
        bodyDef.setPosition(new Vec2(
                fromBufferToMetersX(prop.posX),
                fromBufferToMetersY(prop.posY))
        );
        bodyDef.setType(prop.type);
        bodyDef.setAllowSleep(false);
        phyComp.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(
                toMetersXLength(phyComp.dimX)/2,
                toMetersYLength(phyComp.dimY)/2,
                prop.phyCenterX,
                prop.phyCenterY
                ,0);
        fixtureDef.setShape(box);
        fixtureDef.setDensity(prop.density);
        fixtureDef.setFriction(prop.friction);
        phyComp.addFixture(fixtureDef);

        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }

    private static void setFurniturePhysics(PhysicsComponent phyComp, PhysicsProp prop) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setLinearDamping(1f);

        bodyDef.setPosition(new Vec2(
                fromBufferToMetersX(prop.posX),
                fromBufferToMetersY(prop.posY))
        );
        bodyDef.setType(prop.type);
        phyComp.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(
                toMetersXLength(phyComp.dimX)/2,
                toMetersYLength(phyComp.dimY)/2,
                prop.phyCenterX,
                prop.phyCenterY,
                0);
        fixtureDef.setShape(box);
        fixtureDef.setFriction(prop.friction);
        fixtureDef.setDensity(prop.density);
        phyComp.addFixture(fixtureDef);
        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }

    private static void setFurnitureOnGameGrid(PhysicsProp prop, float dimX, float dimY) {
        SparseArray<Node>  nodes= GameGrid.getInstance().getNodes(prop.posX, prop.posY, (int)dimX, (int)dimY);

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
            GameGrid.getInstance().increaseDefaultCostOnNode(nodes.valueAt(i), cost);
        }
    }

}

