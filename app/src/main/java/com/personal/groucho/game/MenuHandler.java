package com.personal.groucho.game;

import static com.personal.groucho.game.constants.System.debugMode;
import static com.personal.groucho.game.constants.System.fpsCounter;

import android.app.Activity;
import android.widget.ImageButton;
import android.widget.Switch;

import com.personal.groucho.R;

public class MenuHandler {

    public static void handleMainMenu(GameWorld gameWorld) {
        gameWorld.activity.setContentView(R.layout.menu);

        ImageButton newGameButton = gameWorld.activity.findViewById(R.id.newGame);
        ImageButton optionsButton = gameWorld.activity.findViewById(R.id.options);
        ImageButton exitButton = gameWorld.activity.findViewById(R.id.exit);
        newGameButton.setOnClickListener(v -> gameWorld.activity.setContentView(gameWorld.activity.renderView));
        optionsButton.setOnClickListener(v -> handleOptionsMainMenu(gameWorld));
        exitButton.setOnClickListener(v -> gameWorld.activity.finish());
    }

    private static void handleOptionsMainMenu(GameWorld gameWorld){
        gameWorld.activity.setContentView(R.layout.options);

        ImageButton undoButton = gameWorld.activity.findViewById(R.id.undoButton);
        handleOptionsSwitches(gameWorld.activity);

        undoButton.setOnClickListener(v -> handleMainMenu(gameWorld));

    }

    public static void handlePauseMenu(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    gameWorld.activity.setContentView(R.layout.pause);
                    ImageButton resumeButton = gameWorld.activity.findViewById(R.id.resumeGame);
                    ImageButton optionsButton = gameWorld.activity.findViewById(R.id.options);
                    ImageButton exitButton = gameWorld.activity.findViewById(R.id.exit);

                    resumeButton.setOnClickListener( v -> {
                        gameWorld.activity.runOnUiThread(
                                () -> gameWorld.activity.setContentView(gameWorld.activity.renderView));
                        gameWorld.resume();
                    });

                    optionsButton.setOnClickListener(v -> handleOptionsPauseMenu(gameWorld));
                    exitButton.setOnClickListener(v -> {
                        gameWorld.finalize();
                        gameWorld.activity.finish();
                    });
                });
    }

    private static void handleOptionsPauseMenu(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    gameWorld.activity.setContentView(R.layout.options);

                    ImageButton undoButton = gameWorld.activity.findViewById(R.id.undoButton);
                    undoButton.setOnClickListener(v -> handlePauseMenu(gameWorld));

                    handleOptionsSwitches(gameWorld.activity);
                });
    }

    private static void handleOptionsSwitches(Activity activity) {
        Switch debugModeSwitch = activity.findViewById(R.id.debugMode);
        Switch fpsCounterSwitch = activity.findViewById(R.id.fpsCounter);

        debugModeSwitch.setChecked(debugMode);
        debugModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> debugMode = isChecked);

        fpsCounterSwitch.setChecked(fpsCounter);
        fpsCounterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> fpsCounter = isChecked);
    }
}
