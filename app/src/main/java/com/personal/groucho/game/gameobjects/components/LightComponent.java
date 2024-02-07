package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.constants.System.characterDimensionsY;
import static com.personal.groucho.game.constants.Environment.brightness;
import static com.personal.groucho.game.constants.Environment.minLightIntensity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.personal.groucho.game.GameWorld;
import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public class LightComponent extends Component {
    private final GameWorld gameWorld;
    private PositionComponent position = null;
    private float intensity = minLightIntensity;


    @Override
    public ComponentType type() {
        return ComponentType.LIGHT;
    }

    public LightComponent(GameWorld gameWorld) {this.gameWorld = gameWorld;}

    public void draw(Canvas canvas) {
        if (position == null)
            position = (PositionComponent) owner.getComponent(ComponentType.POSITION);
        Bitmap buffer = gameWorld.getBuffer();

        Bitmap maskBitmap = Bitmap.createBitmap(
                buffer.getWidth()+100,
                buffer.getHeight()+100,
                Bitmap.Config.ARGB_8888
        );
        Canvas maskCanvas = new Canvas(maskBitmap);

        maskCanvas.drawColor(Color.valueOf(0,0,0, 1-brightness).toArgb());

        Paint maskPaint = new Paint();
        maskPaint.setAntiAlias(true);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        maskCanvas.drawCircle(
                (float)maskBitmap.getWidth() / 2,
                (float)maskBitmap.getHeight() / 2,
                intensity,
                maskPaint
        );

        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        maskCanvas.drawBitmap(buffer, 0, 0, maskPaint);

        canvas.drawBitmap(
                maskBitmap,
                position.getPosX() - (float)maskBitmap.getWidth() /2,
                (position.getPosY() - (float)maskBitmap.getHeight() /2)+characterDimensionsY,
                null
        );
    }

    public void setLightIntensity(float intensity) {this.intensity = intensity;}
}

