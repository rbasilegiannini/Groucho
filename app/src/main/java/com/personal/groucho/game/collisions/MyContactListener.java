package com.personal.groucho.game.collisions;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;
import com.personal.groucho.game.gameobjects.GameObject;

import java.util.Collection;
import java.util.HashSet;

public class MyContactListener extends ContactListener {

    private final Collection<Collision> collisions = new HashSet<>();

    public Collection<Collision> getCollisions() {
        Collection<Collision> result = new HashSet<>(collisions);
        collisions.clear();
        return result;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
        GameObject a = (GameObject)userdataA,
                b = (GameObject)userdataB;

        // TO DO: use an object pool instead
        collisions.add(new Collision(a, b));
    }
}
