package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class BoxDrawableComponent extends DrawableComponent{
    private Paint paint;
    private float dimX;
    private float dimY;
    private PositionComponent posComp = null;
    private final Rect surface;

    public BoxDrawableComponent(){
        this.surface = new Rect();
    }

    public void init(float dimX, float dimY, Paint paint) {
        this.dimX = dimX;
        this.dimY = dimY;
        this.paint = paint;
    }

    @Override
    public void reset() {
        super.reset();
        this.posComp = null;
    }

    @Override
    public void draw(Canvas canvas) {
        if (posComp == null) {
            posComp = (PositionComponent) owner.getComponent(POSITION);

            surface.left = (int) (posComp.posX - dimX /2);
            surface.top = (int) (posComp.posY - dimY /2);
            surface.right = (int) (surface.left + dimX);
            surface.bottom = (int) (surface.top + dimY);
        }

        canvas.drawRect(surface, paint);
    }
}
