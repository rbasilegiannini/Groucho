package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

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
        PositionComponent posComponent = (PositionComponent) owner.getComponent(POSITION);

        canvas.drawRect(
                (float)posComponent.posX - dimensionX/2,
                (float)posComponent.posY - dimensionY/2,
                posComponent.posX + dimensionX/2,
                posComponent.posY + dimensionY/2,
                paint
        );
    }
}
