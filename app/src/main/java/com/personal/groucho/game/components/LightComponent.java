package com.personal.groucho.game.components;

import static com.personal.groucho.game.Constants.brightness;
import static com.personal.groucho.game.Constants.characterDimensionsY;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.personal.groucho.game.GameWorld;

public class LightComponent extends Component{
    private final GameWorld gameWorld;
    private PositionComponent position = null;
    private float intensity = 150;


    @Override
    public ComponentType type() {
        return ComponentType.Light;
    }

    public LightComponent(GameWorld gameWorld) {this.gameWorld = gameWorld;}

    public void draw(Canvas canvas) {
        if (position == null)
            position = (PositionComponent) owner.getComponent(ComponentType.Position);

        Bitmap maskBitmap = Bitmap.createBitmap(
                gameWorld.buffer.getWidth()+100,
                gameWorld.buffer.getHeight()+100,
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
        maskCanvas.drawBitmap(gameWorld.buffer, 0, 0, maskPaint);

        canvas.drawBitmap(
                maskBitmap,
                position.getPosX() - (float)maskBitmap.getWidth() /2,
                (position.getPosY() - (float)maskBitmap.getHeight() /2)+characterDimensionsY,
                null
        );
    }

    public void setLightIntensity(float intensity) {this.intensity = intensity;}
}

