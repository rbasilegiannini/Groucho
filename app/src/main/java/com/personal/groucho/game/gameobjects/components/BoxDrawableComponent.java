package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class BoxDrawableComponent extends DrawableComponent{

    private final Paint paint;
    private final float dimensionX;
    private final float dimensionY;
    private PositionComponent posComp = null;
    private final Rect surface;

    public BoxDrawableComponent(float dimensionX, float dimensionY, Paint paint) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.paint = paint;
        this.surface = new Rect();
    }

    @Override
    public void draw(Canvas canvas) {
        if (posComp == null) {
            posComp = (PositionComponent) owner.getComponent(POSITION);

            surface.left = (int) (posComp.posX - dimensionX/2);
            surface.top = (int) (posComp.posY - dimensionY/2);
            surface.right = (int) (surface.left + dimensionX);
            surface.bottom = (int) (surface.top + dimensionY);
        }

        canvas.drawRect(surface, paint);
    }
}
