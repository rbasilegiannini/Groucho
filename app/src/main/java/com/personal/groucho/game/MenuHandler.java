package com.personal.groucho.game;

import android.app.Activity;
import android.widget.ImageButton;
import android.widget.Switch;

import com.personal.groucho.R;

public class MenuHandler {

    public static void handleMainMenu(Activity activity, AndroidFastRenderView renderView) {
        activity.setContentView(R.layout.menu);

        ImageButton newGameButton = activity.findViewById(R.id.newGame);
        ImageButton optionsButton = activity.findViewById(R.id.options);
        ImageButton exitButton = activity.findViewById(R.id.exit);
        newGameButton.setOnClickListener(v -> activity.setContentView(renderView));
        optionsButton.setOnClickListener(v -> handleOptionMenu(activity, renderView));
        exitButton.setOnClickListener(v -> activity.finish());
    }

    public static void handleOptionMenu(Activity activity, AndroidFastRenderView renderView){
        activity.setContentView(R.layout.options);

        ImageButton undoButton = activity.findViewById(R.id.undoButton);
        Switch debugModeSwitch = activity.findViewById(R.id.debugMode);
        Switch fpsCounterSwitch = activity.findViewById(R.id.fpsCounter);

        undoButton.setOnClickListener(v -> handleMainMenu(activity, renderView));

        debugModeSwitch.setChecked(com.personal.groucho.game.constants.System.debugMode);
        debugModeSwitch.setOnCheckedChangeListener((
                buttonView, isChecked) -> com.personal.groucho.game.constants.System.debugMode = isChecked);

        fpsCounterSwitch.setChecked(com.personal.groucho.game.constants.System.fpsCounter);
        fpsCounterSwitch.setOnCheckedChangeListener((
                buttonView, isChecked) -> com.personal.groucho.game.constants.System.fpsCounter = isChecked);

    }
}
