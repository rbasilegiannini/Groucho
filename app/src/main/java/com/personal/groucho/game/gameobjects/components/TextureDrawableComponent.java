package com.personal.groucho.game.gameobjects.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.personal.groucho.game.gameobjects.ComponentType;


public class TextureDrawableComponent extends DrawableComponent{

    private PositionComponent position = null;
    private final int dimX, dimY;
    private final Bitmap texture;
    private final Rect src;
    private final Rect dest;

    public TextureDrawableComponent(Bitmap texture, int dimX, int dimY) {
        this.texture = texture;
        src = new Rect(0, 0, texture.getWidth(), texture.getHeight());
        dest = new Rect(-dimX/2, -dimY/2, dimX/2, dimY/2);
        this.dimX = dimX;
        this.dimY = dimY;
    }

    @Override
    public void draw(Canvas canvas) {
        if (position == null) {
            position = (PositionComponent) owner.getComponent(ComponentType.POSITION);
        }

        dest.offsetTo(position.getPosX()-dimX/2, position.getPosY()-dimY/2);
        canvas.drawBitmap(texture, src, dest, null);
    }
}
