package com.personal.groucho.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.personal.groucho.R;

public class MainActivity extends Activity {

    private AndroidFastRenderView renderView;

    private static final float XMIN = -10, XMAX = 10, YMIN = -15, YMAX = 15;


    public static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getString(R.string.app_name);

        loadResources();
        setWindow();

        // Game world
        GameWorld gameWorld = buildGameWorld();
        gameWorld.addGameObject(GameObjectFactory.makePlayer(0,0));

        // View
        renderView = new AndroidFastRenderView(this, gameWorld);
        setContentView(renderView);

        // Touch
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
    }

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
