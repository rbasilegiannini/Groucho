package com.personal.groucho.game.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class TextureDrawableComponent extends DrawableComponent{

    private final int dimX, dimY;
    private final Bitmap texture;
    private final Rect src;
    private Rect dest;

    public TextureDrawableComponent(Bitmap texture, int dimX, int dimY) {
        this.texture = texture;
        src = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        dest = new Rect(-dimX/2, -dimY/2, dimX/2, dimY/2);
        this.dimX = dimX;
        this.dimY = dimY;
    }

    @Override
    public void draw(Canvas canvas) {
        PositionComponent pos = (PositionComponent) owner.getComponent(ComponentType.Position);
        dest.offsetTo(pos.getPosX()-dimX/2, pos.getPosY()-dimY/2);
        canvas.drawBitmap(texture, src, dest, null);
    }
}
