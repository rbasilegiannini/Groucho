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

    public Orientation getOpposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return this;
        }
    }

    public Orientation getTurnOnRight() {
        switch (this) {
            case UP:
                return RIGHT;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            case RIGHT:
                return DOWN;
            default:
                return this;
        }
    }
}
