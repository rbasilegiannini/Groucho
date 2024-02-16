package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.gameobjects.ComponentType.DRAWABLE;

import android.graphics.Canvas;

import com.personal.groucho.game.gameobjects.Component;
import com.personal.groucho.game.gameobjects.ComponentType;

public abstract class DrawableComponent extends Component {
    @Override
    public ComponentType type() {return DRAWABLE;}

    public abstract void draw(Canvas canvas);
}
