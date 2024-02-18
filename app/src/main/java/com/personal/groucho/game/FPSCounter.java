package com.personal.groucho.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.personal.groucho.game.controller.Widget;

public class FPSCounter extends Widget {
    private static FPSCounter instance = null;
    private final Paint paint;
    public float posX, posY;
    public int fps = 0;

    protected FPSCounter() {
        super(0, 0);

        posX = centerX;
        posY = centerY;

        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(60);
    }

    public static FPSCounter getInstance(){
        if (instance == null){
            instance = new FPSCounter();
        }
        return instance;
    }

    @Override
    public void updateWidgetPosition(float increaseX, float increaseY) {
        posX += increaseX;
        posY += increaseY;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("FPS: " + fps, posX, posY, paint);
    }
}
