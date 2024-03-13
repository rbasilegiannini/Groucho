package com.personal.groucho.game.constants;

public class System {
    public static boolean godMode = false;
    public static boolean debugMode = false;
    public static boolean fpsCounter = true;
    public static boolean memoryUsage = true;
    public static final float charDimX = 32;
    public static final float charDimY = 32;
    public static final int charScaleFactor = 5;
    public static float distSight = 1800;
    public static int maxInvisiblePlayer = 5000;
    public static int maxInvestigateTime = 15000;
    public static int cellSize = charScaleFactor *(int) charDimX;
    public static int staticDCost = 10000000;
}
