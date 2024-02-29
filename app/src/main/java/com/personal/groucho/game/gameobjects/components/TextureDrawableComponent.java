package com.personal.groucho.game.gameobjects.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.personal.groucho.game.gameobjects.ComponentType;


public class TextureDrawableComponent extends DrawableComponent{
    private PositionComponent posComp = null;
    private int dimX;
    private int dimY;
    private Bitmap texture;
    private final Rect src;
    private final Rect dest;

    public TextureDrawableComponent(){
        src = new Rect();
        dest = new Rect();
    }

    public void init(Bitmap texture, int dimX, int dimY){
        this.texture = texture;
        this.dimX = dimX;
        this.dimY = dimY;

        src.left = 0;
        src.top = 0;
        src.right = texture.getWidth();
        src.bottom = texture.getHeight();

        dest.left = -dimX/2;
        dest.top = -dimY/2;
        dest.right = dimX/2;
        dest.bottom = dimY/2;
    }

    @Override
    public void reset() {
        super.reset();
        this.posComp = null;
    }

    @Override
    public void draw(Canvas canvas) {
        if (posComp == null) {
            posComp = (PositionComponent) owner.getComponent(ComponentType.POSITION);
        }

        dest.offsetTo(posComp.posX-dimX/2, posComp.posY-dimY/2);
        canvas.drawBitmap(texture, src, dest, null);
    }
}
