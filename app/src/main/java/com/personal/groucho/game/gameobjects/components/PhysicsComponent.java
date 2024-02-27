package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;
import static com.personal.groucho.game.gameobjects.ComponentType.PHYSICS;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class PhysicsComponent extends Component {

    private final World world;
    private PositionComponent posComp = null;
    public Body body;
    public float density, originalPosX, originalPosY;
    public final float dimX, dimY;
    public float fixtureCenterX, fixtureCenterY;

    public PhysicsComponent(World world, float dimX, float dimY){
        this.world = world;
        this.dimX = dimX;
        this.dimY = dimY;
    }

    @Override
    public ComponentType type() {return PHYSICS;}

    @Override
    public void delete() {
        body.setActive(false);
        body.delete();
    }

    public void setBody(BodyDef bodyDef) {
        body = world.createBody(bodyDef);
        body.setUserData(owner);
        originalPosX = body.getPositionX();
        originalPosY = body.getPositionY();
    }

    public void addFixture(FixtureDef fixtureDef) {
        density = fixtureDef.getDensity();
        body.createFixture(fixtureDef);
        fixtureCenterX = fixtureDef.getCenterX();
        fixtureCenterY = fixtureDef.getCenterY();
    }

    public void applyForce(Vec2 force) {
        initComponents();

        body.setLinearVelocity(new Vec2(0,0));
        body.applyLinearImpulse(force,body.getPosition(),true);

        posComp.setPosX((int) fromMetersToBufferX(body.getPositionX()));
        posComp.setPosY((int) fromMetersToBufferY(body.getPositionY()));
    }

    public void setBullet(boolean isBullet) {
        body.setBullet(isBullet);
    }

    public void setPos(int posX, int posY) {
        initComponents();

        body.setTransform(new Vec2(fromBufferToMetersX(posX), fromBufferToMetersY(posY)),0);
        posComp.setPosX((int) fromMetersToBufferX(body.getPositionX()));
        posComp.setPosY((int) fromMetersToBufferY(body.getPositionY()));
    }

    public void setPosX(int posX) {
        initComponents();

        body.setTransform(fromBufferToMetersX(posX), body.getPositionY(), 0);
        posComp.setPosX(posX);
    }

    public void setPosY(int posY) {
        initComponents();

        body.setTransform(body.getPositionX(), fromBufferToMetersY(posY), 0);
        posComp.setPosY(posY);
    }

    public void updatePosX(float increase) {
        initComponents();

        body.setTransform(body.getPositionX() + increase, body.getPositionY(), 0);
        posComp.updatePosX((int)increase);
    }

    public void updatePosY(float increase) {
        initComponents();

        body.setTransform(body.getPositionX(),body.getPositionY() + increase, 0);
        posComp.updatePosY((int)increase);
    }

    public float getPosX() {return body.getPositionX();}
    public float getPosY() {return body.getPositionY();}

    public boolean hasChangedPosition() {
        if (body.getPositionX() != originalPosX || body.getPositionY() != originalPosY) {
            originalPosX = body.getPositionX();
            originalPosY = body.getPositionY();
            return true;
        }
        else {
            return false;
        }
    }

    private void initComponents() {
        if (posComp == null) {
            posComp = (PositionComponent) owner.getComponent(POSITION);
        }
    }
}

