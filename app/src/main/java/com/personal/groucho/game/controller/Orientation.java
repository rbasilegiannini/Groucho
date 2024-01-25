package com.personal.groucho.game.controller;

public enum Orientation {
    UP(0),
    DOWN(2),
    LEFT(1),
    RIGHT(3);

    private final int value;

    Orientation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
