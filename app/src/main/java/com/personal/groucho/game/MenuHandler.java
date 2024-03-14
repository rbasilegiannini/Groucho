package com.personal.groucho.game;

import static com.personal.groucho.game.constants.System.debugMode;
import static com.personal.groucho.game.constants.System.fpsCounter;
import static com.personal.groucho.game.constants.System.godMode;
import static com.personal.groucho.game.constants.System.memoryUsage;

import android.app.Activity;
import android.widget.ImageButton;
import android.widget.Switch;

import com.personal.groucho.R;
import com.personal.groucho.game.levels.first.FirstLevel;

public class MenuHandler {

    public static void handleMainMenu(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    if (gameWorld.activity.backgroundMusic != null)
                        gameWorld.activity.backgroundMusic.pause();

                    gameWorld.activity.setContentView(R.layout.menu);

                    ImageButton newGameButton = gameWorld.activity.findViewById(R.id.newGame);
                    ImageButton optionsButton = gameWorld.activity.findViewById(R.id.options);
                    ImageButton exitButton = gameWorld.activity.findViewById(R.id.exit);
                    newGameButton.setOnClickListener(v -> {
                        gameWorld.init(new FirstLevel(gameWorld));
                        gameWorld.activity.renderView.prepareSurface();
                        gameWorld.activity.setContentView(gameWorld.activity.renderView);
                    });
                    optionsButton.setOnClickListener(v -> handleOptionsMainMenu(gameWorld));
                    exitButton.setOnClickListener(v -> gameWorld.finalize());
                });
    }

    private static void handleOptionsMainMenu(GameWorld gameWorld){
        gameWorld.activity.runOnUiThread(
                () -> {
                    gameWorld.activity.setContentView(R.layout.options);

                    ImageButton undoButton = gameWorld.activity.findViewById(R.id.undoButton);
                    handleOptionsSwitches(gameWorld);

                    undoButton.setOnClickListener(v -> handleMainMenu(gameWorld));
                });
    }

    public static void handlePauseMenu(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    gameWorld.activity.renderView.releaseSurface();
                    gameWorld.activity.setContentView(R.layout.pause);

                    ImageButton resumeButton = gameWorld.activity.findViewById(R.id.resumeGame);
                    ImageButton optionsButton = gameWorld.activity.findViewById(R.id.options);
                    ImageButton exitButton = gameWorld.activity.findViewById(R.id.exit);

                    resumeButton.setOnClickListener( v -> {
                        gameWorld.activity.renderView.prepareSurface();
                        gameWorld.activity.runOnUiThread(
                                () -> gameWorld.activity.setContentView(gameWorld.activity.renderView));
                        gameWorld.resume();
                    });

                    optionsButton.setOnClickListener(v -> handleOptionsPauseMenu(gameWorld));
                    exitButton.setOnClickListener(v -> gameWorld.finalize());
                });
    }

    private static void handleOptionsPauseMenu(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    gameWorld.activity.setContentView(R.layout.options);

                    ImageButton undoButton = gameWorld.activity.findViewById(R.id.undoButton);
                    undoButton.setOnClickListener(v -> handlePauseMenu(gameWorld));

                    handleOptionsSwitches(gameWorld);
                });
    }

    private static void handleOptionsSwitches(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                Switch debugModeSwitch = gameWorld.activity.findViewById(R.id.debugMode);
                Switch fpsCounterSwitch = gameWorld.activity.findViewById(R.id.fpsCounter);
                Switch memoryUsageSwitch = gameWorld.activity.findViewById(R.id.memoryUsage);
                Switch godModeSwitch = gameWorld.activity.findViewById(R.id.godMode);

                debugModeSwitch.setChecked(debugMode);
                debugModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> debugMode = isChecked);

                fpsCounterSwitch.setChecked(fpsCounter);
                fpsCounterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> fpsCounter = isChecked);

                memoryUsageSwitch.setChecked(memoryUsage);
                memoryUsageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> memoryUsage = isChecked);

                godModeSwitch.setChecked(godMode);
                godModeSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> godMode = isChecked));
                });
    }

    public static void handleGameOverMenu(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    gameWorld.activity.renderView.releaseSurface();
                    gameWorld.activity.setContentView(R.layout.gameover);
                    handleTryAgain(gameWorld);
                });
    }


    public static void handleCompleteMenu(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    gameWorld.activity.renderView.releaseSurface();
                    gameWorld.activity.setContentView(R.layout.complete);
                    handleTryAgain(gameWorld);
                });
    }

    public static void handleTryAgain(GameWorld gameWorld) {
        gameWorld.activity.runOnUiThread(
                () -> {
                    ImageButton tryAgainButton = gameWorld.activity.findViewById(R.id.tryAgain);
                    ImageButton exitButton = gameWorld.activity.findViewById(R.id.exit);

                    tryAgainButton.setOnClickListener(v -> handleMainMenu(gameWorld));
                    exitButton.setOnClickListener(v -> gameWorld.finalize());
                });
    }
}
