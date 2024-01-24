package com.personal.groucho.game;

import static com.personal.groucho.game.Constants.characterScaleFactor;
import static com.personal.groucho.game.Constants.grouchoHealth;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.toMetersXLength;
import static com.personal.groucho.game.Utils.toMetersYLength;
import static com.personal.groucho.game.assets.Spritesheets.groucho_death;
import static com.personal.groucho.game.assets.Spritesheets.groucho_walk;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.components.AliveComponent;
import com.personal.groucho.game.components.BoxDrawableComponent;
import com.personal.groucho.game.components.ComponentType;
import com.personal.groucho.game.components.ControllableComponent;
import com.personal.groucho.game.components.PhysicsComponent;
import com.personal.groucho.game.components.PositionComponent;
import com.personal.groucho.game.components.SpriteDrawableComponent;
import com.personal.groucho.game.components.TextureDrawableComponent;

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

    private static final float characterDimensionsX = 32;
    private static final float characterDimensionsY = 32;

    public static GameObject makePlayer(int posX, int posY, Controller controller, GameWorld gameworld) {
        GameObject gameObject = new GameObject("Groucho", Role.PLAYER);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new SpriteDrawableComponent(groucho_walk, groucho_death));
        gameObject.addComponent(new ControllableComponent(controller, gameworld));
        gameObject.addComponent(new PhysicsComponent(gameworld.world));
        gameObject.addComponent(new AliveComponent(grouchoHealth));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.Physics);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 1f, 1f, BodyType.dynamicBody);
        setCharacterPhysics(physics, properties);

        return gameObject;
    }

    public static GameObject makeEnemy(int posX, int posY, int health, Spritesheet idle,
                                       Spritesheet death, World world) {
        GameObject gameObject = new GameObject("Enemy", Role.ENEMY);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(world));
        gameObject.addComponent(new SpriteDrawableComponent(idle, death));
        gameObject.addComponent(new AliveComponent(health));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.Physics);
        PhysicsProperties properties = new PhysicsProperties(posX, posY,1f, 1f, BodyType.dynamicBody);
        setCharacterPhysics(physics, properties);

        return gameObject;
    }

    private static void setCharacterPhysics(PhysicsComponent physics, PhysicsProperties properties) {
        BodyDef bodyDef = new BodyDef();
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
                0,1.6f,0
        );

        fixtureDef.setShape(box);
        fixtureDef.setDensity(properties.density);
        fixtureDef.setFriction(properties.friction);
        physics.addFixture(fixtureDef);

        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }

    public static GameObject makeWall(int posX, int posY, float dimX, float dimY, World world) {
        GameObject gameObject = new GameObject("Wall", Role.WALL);

        Paint paint = new Paint();
        paint.setColor(Color.valueOf(188, 143, 143).toArgb());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(world));
        gameObject.addComponent(
                new BoxDrawableComponent(dimX, dimY, paint)
        );

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.Physics);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 0f, 0f, BodyType.staticBody);
        setFurniturePhysics(physics, properties, dimX, dimY);

        return gameObject;
    }

    public static GameObject makeFurniture(int posX, int posY, float dimX, float dimY, World world,
                                           Bitmap texture) {
        GameObject gameObject = new GameObject("Furniture", Role.FURNITURE);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(world));
        gameObject.addComponent(new TextureDrawableComponent(texture, (int)dimX, (int)dimY));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.Physics);
        PhysicsProperties properties = new PhysicsProperties(posX, posY, 1f, 2f, BodyType.dynamicBody);
        setFurniturePhysics(physics, properties, dimX, dimY);

        return gameObject;
    }

    private static void setFurniturePhysics(PhysicsComponent physics, PhysicsProperties properties,
                                            float dimX, float dimY) {
        BodyDef bodyDef = new BodyDef();
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
}

