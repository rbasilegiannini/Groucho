package com.personal.groucho.game.collisions;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.personal.groucho.game.gameobjects.GameObject;

import java.util.Collection;
import java.util.HashSet;

public class MyContactListener extends ContactListener {
    private final Collection<Collision> collisions = new HashSet<>();
    private Body ba, bb;

    public Collection<Collision> getCollisions() {
        Collection<Collision> result = new HashSet<>(collisions);
        collisions.clear();
        return result;
    }

    @Override
    // TODO: use an object pool instead
    public void beginContact(Contact contact) {
        ba = contact.getFixtureA().getBody();
        bb = contact.getFixtureB().getBody();

        GameObject a = (GameObject) ba.getUserData();
        GameObject b = (GameObject) bb.getUserData();

        collisions.add(new Collision(a, b));
    }
}
