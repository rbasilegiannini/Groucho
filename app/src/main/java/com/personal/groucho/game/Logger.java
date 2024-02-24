package com.personal.groucho.game;

import static com.personal.groucho.game.constants.System.fpsCounter;
import static com.personal.groucho.game.constants.System.memoryUsage;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;

import com.personal.groucho.game.controller.Widget;

public class Logger extends Widget {
    private static Logger instance = null;
    private final Paint paint;
    public float posX, posY;
    public int fps = 0;
    private final Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
    private long currentMillis = System.currentTimeMillis();

    protected Logger() {
        super(0, 0);

        posX = centerX;
        posY = centerY;

        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(60);
    }

    public static Logger getInstance(){
        if (instance == null){
            instance = new Logger();
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
        if (fpsCounter) {
            drawFPS(canvas);
        }
        if (memoryUsage) {
            drawMemoryUsage(canvas);
        }
    }

    private void drawMemoryUsage(Canvas canvas) {
        if (System.currentTimeMillis() - currentMillis > 1000) {
            Debug.getMemoryInfo(memoryInfo);
            currentMillis = System.currentTimeMillis();
        }
        canvas.drawText("MEM: " + memoryInfo.getTotalPss()/1000 + " MB",
                posX, posY + paint.getTextSize(), paint);
    }

    private void drawFPS(Canvas canvas) {
        canvas.drawText("FPS: " + fps, posX, posY, paint);
    }
}
