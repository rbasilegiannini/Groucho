package com.personal.groucho.game;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.personal.groucho.R;
import com.personal.groucho.badlogic.androidgames.framework.Music;
import com.personal.groucho.badlogic.androidgames.framework.impl.AndroidAudio;
import com.personal.groucho.badlogic.androidgames.framework.impl.MultiTouchHandler;
import com.personal.groucho.badlogic.androidgames.framework.Audio;
import com.personal.groucho.game.assets.Sounds;
import com.personal.groucho.game.assets.Spritesheets;
import com.personal.groucho.game.assets.Textures;
import com.personal.groucho.game.constants.Character;
import com.personal.groucho.game.constants.Environment;

public class MainActivity extends Activity {

    protected AndroidFastRenderView renderView;
    private Audio audio;
    protected Music backgroundMusic;

    private MultiTouchHandler touch;

    private static final float XMIN = -15, XMAX = 15, YMIN = -10, YMAX = 10;

    public static String TAG;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getString(R.string.app_name);

        loadResources();
        initConstants();

        setWindow();
        GameWorld gameWorld = buildGameWorld();

        // View
        renderView = new AndroidFastRenderView(this, gameWorld);
        MenuHandler.handleMainMenu(gameWorld);

        // Touch
        touch = new MultiTouchHandler(renderView, 1, 1);
        gameWorld.setTouchHandler(touch);

        // Sound
        audio = new AndroidAudio(this);
        Sounds.init(audio);
        backgroundMusic = audio.newMusic("lavender_town.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.2f);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void initConstants() {
        Character.init(this);
        Environment.init(this);
    }

    @NonNull
    private GameWorld buildGameWorld() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Box physicalSize = new Box(XMIN, YMIN, XMAX, YMAX);
        Box screenSize = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);

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

    private boolean wasPlaying = false;
    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");

        if (backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
            wasPlaying = true;
        }

        if (renderView != null && renderView.isRunning()) {
            renderView.pause();
        }
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
        if (wasPlaying){
            backgroundMusic.play();
            wasPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Main thread", "destroy");

        Spritesheets.release();
        Textures.release();
        Sounds.release();

        if (renderView != null) {
            renderView.pause();
            renderView = null;
        }
        if (audio != null) {
            audio = null;
        }
        if (touch != null) {
            touch = null;
        }
    }

}
