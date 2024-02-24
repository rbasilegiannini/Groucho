package com.personal.groucho.game.collisions;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.GameObject;

import java.util.Collection;
import java.util.HashSet;

public class MyContactListener extends ContactListener {

    // TODO: Use SparseArray
    private final Collection<Collision> collisions = new HashSet<>();
    private Body ba, bb;
    private final GameWorld gameWorld;

    public MyContactListener(GameWorld gameWorld) {this.gameWorld = gameWorld;}

    public Collection<Collision> getCollisions() {
        Collection<Collision> result = new HashSet<>(collisions);
        collisions.clear();
        return result;
    }

    @Override
    public void beginContact(Contact contact) {
        ba = contact.getFixtureA().getBody();
        bb = contact.getFixtureB().getBody();

        GameObject a = (GameObject) ba.getUserData();
        GameObject b = (GameObject) bb.getUserData();

        Collision collision = gameWorld.collisionsPool.acquire();
        collision.GO1 = a;
        collision.GO2 = b;

        collisions.add(collision);
    }
}
