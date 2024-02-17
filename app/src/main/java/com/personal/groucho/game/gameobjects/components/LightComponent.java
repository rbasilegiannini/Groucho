package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.constants.System.characterDimY;
import static com.personal.groucho.game.constants.Environment.brightness;
import static com.personal.groucho.game.constants.Environment.minLightIntensity;
import static com.personal.groucho.game.gameobjects.ComponentType.LIGHT;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class LightComponent extends Component {
    private PositionComponent position = null;
    private final Paint maskPaint;
    private Bitmap maskBitmap, defaultBitmap;
    private final Bitmap buffer;
    private final Canvas maskCanvas ;
    private final PorterDuffXfermode porterCLEAR;
    private float intensity = minLightIntensity;
    private final float centerX, centerY;

    @Override
    public ComponentType type() {
        return LIGHT;
    }

    public LightComponent(Bitmap buffer) {
        this.maskPaint = new Paint();
        porterCLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        this.buffer = buffer;

        defaultBitmap = Bitmap.createBitmap(
                buffer.getWidth()+100,
                buffer.getHeight()+100,
                Bitmap.Config.ARGB_8888
        );

        maskCanvas = new Canvas(defaultBitmap);
        centerX = (float)defaultBitmap.getWidth() / 2;
        centerY = (float)(defaultBitmap.getHeight() / 2) - characterDimY;
    }

    @SuppressLint("NewApi")
    public void draw(Canvas canvas) {
        if (position == null) {
            position = (PositionComponent) owner.getComponent(POSITION);
        }

        maskBitmap = Bitmap.createBitmap(defaultBitmap);
        maskCanvas.setBitmap(maskBitmap);

        maskCanvas.drawColor(Color.valueOf(0,0,0, 1-brightness).toArgb());

        maskPaint.setXfermode(porterCLEAR);
        maskCanvas.drawCircle(centerX, centerY, intensity, maskPaint);

        canvas.drawBitmap(maskBitmap, position.posX - centerX, position.posY - centerY, null);
    }

    public void setLightIntensity(float intensity) {this.intensity = intensity;}
}

