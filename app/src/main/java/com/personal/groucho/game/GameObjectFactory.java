package com.personal.groucho.game;

import static com.personal.groucho.game.Constants.characterScaleFactor;
import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.toMetersXLength;
import static com.personal.groucho.game.Utils.toMetersYLength;

import android.graphics.Color;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.components.BoxDrawableComponent;
import com.personal.groucho.game.components.ComponentType;
import com.personal.groucho.game.components.ControllableComponent;
import com.personal.groucho.game.components.PhysicsComponent;
import com.personal.groucho.game.components.PositionComponent;
import com.personal.groucho.game.components.SpriteDrawableComponent;

public class GameObjectFactory {

    private static final float characterDimensionsX = 32;
    private static final float characterDimensionsY = 64;

    public static GameObject makePlayer(int posX, int posY, Controller controller, GameWorld gameworld) {
        GameObject gameObject = new GameObject("Groucho", Role.PLAYER);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new SpriteDrawableComponent(Spritesheets.groucho_walk));
        gameObject.addComponent(new ControllableComponent(controller, gameworld));
        gameObject.addComponent(new PhysicsComponent(gameworld.world));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.Physics);
        setCharacterPhysics(posX, posY, physics, BodyType.dynamicBody);

        return gameObject;
    }

    public static GameObject makeEnemy(int posX, int posY, Spritesheet idle, World world) {
        GameObject gameObject = new GameObject("Enemy", Role.ENEMY);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(world));
        gameObject.addComponent(new SpriteDrawableComponent(idle));

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.Physics);
        setCharacterPhysics(posX, posY, physics, BodyType.dynamicBody);

        return gameObject;
    }

    private static void setCharacterPhysics(int posX, int posY, PhysicsComponent physics, BodyType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(new Vec2(fromBufferToMetersX(posX), fromBufferToMetersY(posY)));
        bodyDef.setType(type);
        bodyDef.setAllowSleep(false);
        physics.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(
                (characterScaleFactor*toMetersXLength(characterDimensionsX))/2,
                (characterScaleFactor*toMetersYLength(characterDimensionsY))/2,
                0,0.6f,0
        );

        fixtureDef.setShape(box);
        fixtureDef.setDensity(1f);
        fixtureDef.setFriction(1f);
        physics.addFixture(fixtureDef);

        box.delete();
        bodyDef.delete();
        fixtureDef.delete();
    }

    public static GameObject makeWall(int posX, int posY, float dimX, float dimY, World world) {
        GameObject gameObject = new GameObject("Wall", Role.WALL);

        gameObject.addComponent(new PositionComponent(posX, posY));
        gameObject.addComponent(new PhysicsComponent(world));
        gameObject.addComponent(
                new BoxDrawableComponent(dimX, dimY, Color.valueOf(188, 143, 143))
        );

        PhysicsComponent physics = (PhysicsComponent) gameObject.getComponent(ComponentType.Physics);

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(new Vec2(fromBufferToMetersX(posX), fromBufferToMetersY(posY)));
        bodyDef.setType(BodyType.staticBody);
        physics.setBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape box = new PolygonShape();
        box.setAsBox(toMetersXLength(dimX)/2, toMetersYLength(dimY)/2);
        fixtureDef.setShape(box);
        physics.addFixture(fixtureDef);

        box.delete();
        bodyDef.delete();
        fixtureDef.delete();

        return gameObject;
    }
}
