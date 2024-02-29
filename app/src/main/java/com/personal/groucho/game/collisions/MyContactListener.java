package com.personal.groucho.game.collisions;

import android.util.SparseArray;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.Pools;
import com.personal.groucho.game.gameobjects.GameObject;

public class MyContactListener extends ContactListener {
    private final SparseArray<Collision> collisions = new SparseArray<>();
    private final SparseArray<Collision> result = new SparseArray<>();

    public SparseArray<Collision> getCollisions() {
        result.clear();
        for (int i = 0; i < collisions.size(); i++) {
            result.put(collisions.keyAt(i), collisions.valueAt(i));
        }
        collisions.clear();

        return result;
    }

    @Override
    public void beginContact(Contact contact) {
        Body ba = contact.getFixtureA().getBody();
        Body bb = contact.getFixtureB().getBody();

        GameObject a = (GameObject) ba.getUserData();
        GameObject b = (GameObject) bb.getUserData();

        Collision collision = Pools.collisionsPool.acquire();
        collision.GO1 = a;
        collision.GO2 = b;

        collisions.put(collision.hashCode(), collision);
    }
}
