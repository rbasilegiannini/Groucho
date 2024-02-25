package com.personal.groucho.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    private final GameWorld gameWorld;

    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public AndroidFastRenderView(Context context, GameWorld gameWorld) {
        super(context);
        this.gameWorld = gameWorld;
        this.framebuffer = gameWorld.graphics.buffer;
        this.holder = getHolder();
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime(), fpsTime = startTime, frameCounter = 0, currentTime;
        float deltaTime, fpsDeltaTime;
        while(running) {
            if(!holder.getSurface().isValid()) {
                continue;
            }

            deltaTime = (System.nanoTime()-startTime) / 1000000000.0f;
            currentTime = System.nanoTime();
            fpsDeltaTime = (currentTime-fpsTime) / 1000000000f;
            startTime = System.nanoTime();

            gameWorld.processInputs();
            gameWorld.update(deltaTime);
            gameWorld.render();

            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.getClipBounds(dstRect);
                canvas.drawBitmap(framebuffer, null, dstRect, null);
                holder.unlockCanvasAndPost(canvas);
            }

            // Measure FPS
            frameCounter++;
            if (fpsDeltaTime > 1) { // Print every second
                Logger.getInstance().fps = (int)frameCounter;
                frameCounter = 0;
                fpsTime = currentTime;
            }
        }
    }

    public void pause() {
        running = false;
        while(true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    }
}

