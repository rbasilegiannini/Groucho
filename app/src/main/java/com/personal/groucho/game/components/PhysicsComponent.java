package com.personal.groucho.game.components;

import static com.personal.groucho.game.Utils.fromBufferToMetersX;
import static com.personal.groucho.game.Utils.fromBufferToMetersY;
import static com.personal.groucho.game.Utils.fromMetersToBufferX;
import static com.personal.groucho.game.Utils.fromMetersToBufferY;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

public class PhysicsComponent extends Component{

    private final World world;
    PositionComponent positionComponent = null;
    private Body body;

    public PhysicsComponent(World world){
        this.world = world;
    }

    @Override
    public ComponentType type() {return ComponentType.Physics;}

    public void setBody(BodyDef bodyDef) {
        body = world.createBody(bodyDef);
        body.setUserData(this.owner);
    }

    public void addFixture(FixtureDef fixtureDef) {
        body.createFixture(fixtureDef);
    }

    public void applyForce(Vec2 force) {
        if(positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        body.applyForceToCenter(force, true);
        positionComponent.setPosX((int) fromMetersToBufferX(body.getPositionX()));
        positionComponent.setPosY((int) fromMetersToBufferY(body.getPositionY()));
    }

    public void setBullet(boolean isBullet) {
        body.setBullet(isBullet);
    }

    public void setPosition(int posX, int posY) {
        if(positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        body.setTransform(new Vec2(fromBufferToMetersX(posX), fromBufferToMetersY(posY)),0);
        positionComponent.setPosX((int) fromMetersToBufferX(body.getPositionX()));
        positionComponent.setPosY((int) fromMetersToBufferY(body.getPositionY()));
    }

    public void updatePosX(float  increase) {
        if(positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        float newPosX = body.getPositionX() + increase;
        body.setTransform(newPosX, body.getPositionY(), 0);
        positionComponent.updatePosX((int)increase);
    }

    public void updatePosY(float increase) {
        if(positionComponent == null)
            positionComponent = (PositionComponent) owner.getComponent(ComponentType.Position);

        float newPosY = body.getPositionY() + increase;
        body.setTransform(body.getPositionX(),newPosY, 0);
        positionComponent.updatePosY((int)increase);
    }

    public float getPositionX() {return body.getPositionX();}
    public float getPositionY() {return body.getPositionY();}
}
