package com.personal.groucho.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.personal.groucho.R;
import com.personal.groucho.badlogic.androidgames.framework.impl.AndroidAudio;
import com.personal.groucho.badlogic.androidgames.framework.impl.MultiTouchHandler;
import com.personal.groucho.badlogic.androidgames.framework.Audio;
import com.personal.groucho.game.assets.Sounds;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.menu.MenuView;

public class MainActivity extends Activity {

    private AndroidFastRenderView renderView;
    private Audio audio;
    private MultiTouchHandler touch;

    private static final float XMIN = -15, XMAX = 15, YMIN = -10, YMAX = 10;

    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getString(R.string.app_name);

        loadResources();
        setWindow();
        GameWorld gameWorld = buildGameWorld();

        // View
        setContentView(R.layout.menu);
        renderView = new AndroidFastRenderView(this, gameWorld);
        handleMainMenu();

        // Touch
        touch = new MultiTouchHandler(renderView, 1, 1);
        gameWorld.setTouchHandler(touch);

        // Sound
        audio = new AndroidAudio(this);
        Sounds.init(audio);
    }

    private void handleMainMenu() {
        ImageButton newGameButton = findViewById(R.id.newGame);
        ImageButton optionsButton = findViewById(R.id.options);
        ImageButton exitButton = findViewById(R.id.exit);
        newGameButton.setOnClickListener(v -> setContentView(renderView));
//        optionsButton.setOnClickListener(v -> setContentView(R.layout.options));
        exitButton.setOnClickListener(v -> finish());
    }

    @NonNull
    private GameWorld buildGameWorld() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Box physicalSize = new Box(XMIN, YMIN, XMAX, YMAX);
        Box screenSize   = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);

        return new GameWorld(physicalSize, screenSize, this);
    }

    private void setWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void loadResources() {
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");
        Spritesheets.load(getResources());
        Textures.load(getResources());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.layout.menu, menu);
//        return true;
//    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");

        renderView.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Main thread", "stop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Main thread", "resume");

        renderView.resume();
    }
}
