package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.Constants.characterScaleFactor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.personal.groucho.game.Spritesheet;

public class SpriteDrawableComponent extends DrawableComponent {
    private Spritesheet currentSpritesheet;
    private final Spritesheet deathSpritesheet;
    private final Paint spriteColor;
    private PositionComponent position = null;

    private int currentAnimation;
    private int currentStep;
    private long lastTimestamp;


    public SpriteDrawableComponent (Spritesheet currentSpritesheet, Spritesheet deathSpritesheet) {
        this.currentSpritesheet = currentSpritesheet;
        this.deathSpritesheet = deathSpritesheet;

        this.spriteColor = new Paint();
        spriteColor.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));

        this.currentAnimation = 0;
        this.currentStep = 0;
        this.lastTimestamp = 0;
    }

    public void setCurrentSpritesheet(Spritesheet sheet) { currentSpritesheet = sheet;}
    public void setAnimation(int animation) {
        currentAnimation = animation;
    }
    public void setStep(int step) { currentStep = step;}
    public void setDeathSpritesheet() {currentSpritesheet = deathSpritesheet;}

    public void updateColorFilter(int currentHealth, int maxHealth) {
        int greenAndBlue = (int)(255 * (float) currentHealth /maxHealth);
        int newColor = Color.argb(255, 255, greenAndBlue, greenAndBlue);
        spriteColor.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.MULTIPLY));
    }

    @Override
    public void draw(Canvas canvas) {
        if (position == null)
            position = (PositionComponent) owner.getComponent(ComponentType.Position);

        long currentTimeMillis = System.currentTimeMillis();
        long delay = currentTimeMillis - lastTimestamp;

        if (delay > currentSpritesheet.getDelay(currentAnimation)) {
            currentStep = currentStep + 1;
            lastTimestamp = currentTimeMillis;
        }

        currentStep = currentSpritesheet.drawAnimation(
                canvas,
                currentAnimation,
                currentStep,
                position.getPosX(), position.getPosY(),
                characterScaleFactor,
                spriteColor
        );
    }
}
