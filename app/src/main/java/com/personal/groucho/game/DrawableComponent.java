package com.personal.groucho.game;

import android.graphics.Canvas;

abstract class DrawableComponent extends Component{
    @Override
    public ComponentType type() {
        return ComponentType.Drawable;
    }

    public abstract void draw(Canvas canvas);
}
