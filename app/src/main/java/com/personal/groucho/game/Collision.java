package com.personal.groucho.game;

public class Collision {
    GameObject GO1, GO2;

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
}
