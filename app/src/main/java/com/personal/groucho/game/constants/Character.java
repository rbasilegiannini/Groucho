package com.personal.groucho.game.constants;

import static com.personal.groucho.game.constants.System.godMode;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.personal.groucho.R;

public class Character {

    public static float grouchoSpeed;
    public static int grouchoHealth, grouchoPower;
    public static float skeletonSpeed;
    public static int skeletonHealth, skeletonPower;
    public static float zombieSpeed;
    public static int zombieHealth, zombiePower;
    public static float wolfSpeed;
    public static int wolfHealth, wolfPower;
    public static int medicalKit;
    public static int hearingRange;
    public static int hearingRangeSqr;
    public static float enemyFovInRad;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void init(Context context){
        grouchoSpeed = context.getResources().getFloat(R.dimen.groucho_speed);
        grouchoHealth = context.getResources().getInteger(R.integer.groucho_health);
        if (godMode) grouchoHealth = 100000000;
        grouchoPower = context.getResources().getInteger(R.integer.groucho_power);

        skeletonSpeed = context.getResources().getFloat(R.dimen.skeleton_speed);
        skeletonHealth = context.getResources().getInteger(R.integer.skeleton_health);
        skeletonPower = context.getResources().getInteger(R.integer.skeleton_power);

        zombieSpeed = context.getResources().getFloat(R.dimen.zombie_speed);
        zombieHealth = context.getResources().getInteger(R.integer.zombie_health);
        zombiePower = context.getResources().getInteger(R.integer.zombie_power);

        wolfSpeed = context.getResources().getFloat(R.dimen.wolf_speed);
        wolfHealth = context.getResources().getInteger(R.integer.wolf_health);
        wolfPower = context.getResources().getInteger(R.integer.wolf_power);

        medicalKit = context.getResources().getInteger(R.integer.medical_kit);

        hearingRange = context.getResources().getInteger(R.integer.hearing_range);
        hearingRangeSqr = context.getResources().getInteger(R.integer.hearing_range_sqr);
        enemyFovInRad = context.getResources().getFloat(R.dimen.enemy_fov_in_rad);
    }
}
