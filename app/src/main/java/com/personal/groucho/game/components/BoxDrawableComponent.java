package com.personal.groucho.game.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BoxDrawableComponent extends DrawableComponent{

    private Color color;
    private float dimensionX, dimensionY;

    public BoxDrawableComponent(float dimensionX, float dimensionY, Color color) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas){
        PositionComponent pos = (PositionComponent) owner.getComponent(ComponentType.Position);

        Paint paint = new Paint();
        paint.setColor(color.toArgb());
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        canvas.drawRect(
                (float)pos.getPosX() - dimensionX/2,
                (float)pos.getPosY() - dimensionY/2,
                pos.getPosX() + dimensionX/2,
                pos.getPosY() + dimensionY/2,
                paint
        );
    }
}
