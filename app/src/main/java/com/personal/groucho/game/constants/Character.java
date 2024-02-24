package com.personal.groucho.game.constants;

import android.content.Context;

import com.personal.groucho.R;

public class Character {

    public static float grouchoSpeed;
    public static float skeletonSpeed;
    public static int grouchoHealth;
    public static int skeletonHealth;
    public static int medicalKit;
    public static int grouchoPower;
    public static int skeletonPower;
    public static int hearingRange;
    public static int hearingRangeSqr;
    public static float enemyFovInRad;

    public static void init(Context context){
        grouchoSpeed = context.getResources().getFloat(R.dimen.groucho_speed);
        grouchoHealth = context.getResources().getInteger(R.integer.groucho_health);
        grouchoPower = context.getResources().getInteger(R.integer.groucho_power);

        skeletonSpeed = context.getResources().getFloat(R.dimen.skeleton_speed);
        skeletonHealth = context.getResources().getInteger(R.integer.skeleton_health);
        skeletonPower = context.getResources().getInteger(R.integer.skeleton_power);

        medicalKit = context.getResources().getInteger(R.integer.medical_kit);

        hearingRange = context.getResources().getInteger(R.integer.hearing_range);
        hearingRangeSqr = context.getResources().getInteger(R.integer.hearing_range_sqr);
        enemyFovInRad = context.getResources().getFloat(R.dimen.enemy_fov_in_rad);
    }
}
