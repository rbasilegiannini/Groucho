package com.personal.groucho.game.gameobjects.components;

import static com.personal.groucho.game.constants.System.characterScaleFactor;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.personal.groucho.game.Spritesheet;
import com.personal.groucho.game.gameobjects.ComponentType;

public class SpriteDrawableComponent extends DrawableComponent {
    private Spritesheet currentSpritesheet;
    private final Spritesheet deathSpritesheet;
    private final Paint spriteColor;
    private PositionComponent posComponent = null;
    private int currentAnim = 0;
    private int currentStep = 0;
    private long lastTimestamp = 0;


    public SpriteDrawableComponent (Spritesheet currentSpritesheet, Spritesheet deathSpritesheet) {
        this.currentSpritesheet = currentSpritesheet;
        this.deathSpritesheet = deathSpritesheet;

        spriteColor = new Paint();
        spriteColor.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
    }

    public void setCurrentSpritesheet(Spritesheet sprite) { currentSpritesheet = sprite;}
    public void setAnim(int anim) {
        currentAnim = anim;
    }
    public void setStep(int step) { currentStep = step;}
    public void setDeathSpritesheet() {
        currentSpritesheet = deathSpritesheet;
        currentAnim = 0;
    }

    public void updateColorFilter(int currentHealth, int maxHealth) {
        int greenAndBlue = (int)(255 * (float) currentHealth /maxHealth);
        int newColor = Color.argb(255, 255, greenAndBlue, greenAndBlue);
        spriteColor.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.MULTIPLY));
    }

    @Override
    public void draw(Canvas canvas) {
        if (posComponent == null) {
            posComponent = (PositionComponent) owner.getComponent(POSITION);
        }
        long currentTimeMillis = System.currentTimeMillis();
        long delay = currentTimeMillis - lastTimestamp;

        if (delay > currentSpritesheet.getDelay(currentAnim)) {
            currentStep = currentStep + 1;
            lastTimestamp = currentTimeMillis;
        }

        currentStep = currentSpritesheet.drawAnimation(
                canvas,
                currentAnim,
                currentStep,
                posComponent.getPosX(), posComponent.getPosY(),
                characterScaleFactor,
                spriteColor
        );
    }
}
