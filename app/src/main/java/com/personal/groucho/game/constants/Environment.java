package com.personal.groucho.game.constants;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.personal.groucho.R;

public class Environment {

    public static float maxLightIntensity;
    public static float minLightIntensity;
    public static float minBrightness;
    public static float maxBrightness;
    public static float brightness;

    public static void init(Context context) {
        maxLightIntensity = context.getResources().getFloat(R.dimen.max_light_intensity);
        minLightIntensity = context.getResources().getFloat(R.dimen.min_light_intensity);
        minBrightness = context.getResources().getFloat(R.dimen.min_brightness);
        maxBrightness = context.getResources().getFloat(R.dimen.max_brightness);
        brightness = maxBrightness;
    }
}
