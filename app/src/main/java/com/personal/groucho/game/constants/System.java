package com.personal.groucho.game.constants;

public class System {
    public static boolean debugMode = true;
    public static boolean fpsCounter = true;
    public static boolean memoryUsage = true;
    public static final float characterDimX = 32;
    public static final float characterDimY = 32;
    public static final int characterScaleFactor = 5;
    public static float distSight = 1000;
    public static int maxInvisiblePlayer = 5000;
    public static int cellSize = characterScaleFactor*(int) characterDimX;
}
