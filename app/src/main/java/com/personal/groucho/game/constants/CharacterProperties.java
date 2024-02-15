package com.personal.groucho.game.constants;

public class CharacterProperties {
    public static float grouchoSpeed = 0.2f;
    public static float skeletonSpeed = 0.05f;
    public static final int grouchoHealth = 100;
    public static final int skeletonHealth = 100;
    public static int medicalKit = 20;
    public static int grouchoPower = 30;
    public static int skeletonPower = 5;
    public static int hearingRange = 1500;
    public static int hearingRangeSqr = (int) Math.pow(hearingRange,2);
    public static float enemyFovInRad = (float) Math.toRadians(120);
}
