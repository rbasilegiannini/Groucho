package com.personal.groucho.game.gameobjects;

public enum ComponentType {
    PHYSICS,
    AI,
    DRAWABLE,
    POSITION,
    CONTROLLABLE,
    LIGHT,
    CHARACTER,
    ALIVE,
    TRIGGER;

    public static ComponentType[] getAllComponentTypes() {
        return new ComponentType[]{POSITION, DRAWABLE, PHYSICS, ALIVE, AI, LIGHT, CONTROLLABLE, CHARACTER, TRIGGER};
    }
}
