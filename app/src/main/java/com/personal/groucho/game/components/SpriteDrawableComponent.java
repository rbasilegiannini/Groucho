package com.personal.groucho.game.components;

import android.graphics.Canvas;

import com.personal.groucho.game.Spritesheet;

public class SpriteDrawableComponent extends DrawableComponent {
    private final Spritesheet spritesheet;
    private int currentAnimation;
    private int currentStep;
    private long lastTimestamp;

    public SpriteDrawableComponent (Spritesheet spritesheet) {
        this.spritesheet = spritesheet;
        this.currentAnimation = 0;
        this.currentStep = 0;
        this.lastTimestamp = 0;
    }

    public void setAnimation(int animation) {
        currentAnimation = animation;
    }

    public void setStep(int step) {
        currentStep = step;
    }

    @Override
    public void draw(Canvas canvas) {
        PositionComponent pos = (PositionComponent) owner.getComponent(ComponentType.Position);
        long currentTimeMillis = System.currentTimeMillis();
        long delay = currentTimeMillis - lastTimestamp;

        if (delay > spritesheet.getDelay(currentAnimation)) {
            currentStep = currentStep + 1;
            lastTimestamp = currentTimeMillis;
        }

        currentStep = spritesheet.drawAnimation(
                canvas, currentAnimation,
                currentStep, pos.getPosX(), pos.getPosY()
        );
    }
}
