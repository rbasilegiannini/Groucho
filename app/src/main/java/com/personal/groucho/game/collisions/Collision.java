package com.personal.groucho.game.collisions;

import com.personal.groucho.game.gameobjects.GameObject;
import com.personal.groucho.game.gameobjects.Resettable;

public class Collision implements Resettable {
    public GameObject GO1, GO2;

    public Collision() {
        GO1 = null;
        GO2 = null;
    }

    public Collision(GameObject GO1, GameObject GO2) {
        this.GO1 = GO1;
        this.GO2 = GO2;
    }

    public int hashCode() {
        return GO1.hashCode() ^ GO2.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof Collision))
            return false;
        Collision otherCollision = (Collision) other;
        return (GO1.equals(otherCollision.GO1) && GO2.equals(otherCollision.GO2)) ||
                (GO1.equals(otherCollision.GO2) && GO2.equals(otherCollision.GO1));
    }

    @Override
    public void reset() {
        GO1 = null;
        GO2 = null;
    }
}
