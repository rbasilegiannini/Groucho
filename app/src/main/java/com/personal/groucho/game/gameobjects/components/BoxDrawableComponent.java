package com.personal.groucho.game.gameobjects.components;

import android.graphics.Canvas;
import android.graphics.Paint;

public class BoxDrawableComponent extends DrawableComponent{

    private final Paint paint;
    private final float dimensionX;
    private final float dimensionY;

    public BoxDrawableComponent(float dimensionX, float dimensionY, Paint paint) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.paint = paint;
    }

    @Override
    public void draw(Canvas canvas){
        PositionComponent pos = (PositionComponent) owner.getComponent(ComponentType.Position);

        canvas.drawRect(
                (float)pos.getPosX() - dimensionX/2,
                (float)pos.getPosY() - dimensionY/2,
                pos.getPosX() + dimensionX/2,
                pos.getPosY() + dimensionY/2,
                paint
        );
    }
}
