package com.personal.groucho.game.components;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

public class PhysicsComponent extends Component{

    private final World world;
    private Body body;

    public PhysicsComponent(World world){
        this.world = world;
    }

    @Override
    public ComponentType type() {return ComponentType.Physics;}

    public void setBody(BodyDef bodyDef) {
        body = world.createBody(bodyDef);
    }

    public void addFixture(FixtureDef fixtureDef) {
        body.createFixture(fixtureDef);
    }

    public void applyForce(Vec2 force) {
        body.applyForceToCenter(force, true);
    }

    public void setBullet(boolean isBullet) {
        body.setBullet(isBullet);
    }
}
