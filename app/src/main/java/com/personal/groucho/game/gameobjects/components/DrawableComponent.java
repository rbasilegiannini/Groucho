package com.personal.groucho.game.gameobjects.components;

import android.graphics.Canvas;

public abstract class DrawableComponent extends Component{
    @Override
    public ComponentType type() {
        return ComponentType.Drawable;
    }

    public abstract void draw(Canvas canvas);
}