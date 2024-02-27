package com.personal.groucho.game.gameobjects.components;

import static android.graphics.PorterDuff.Mode.MULTIPLY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;
import static com.personal.groucho.game.gameobjects.ComponentType.POSITION;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;

import com.personal.groucho.game.Spritesheet;

public class SpriteDrawableComponent extends DrawableComponent {
    private Spritesheet currentSpritesheet;
    private final Paint spriteColor;
    private PositionComponent posComp = null;
    private int currentAnim = 0, currentStep = 0;
    private long lastTimestamp = 0;


    public SpriteDrawableComponent (Spritesheet currentSpritesheet) {
        this.currentSpritesheet = currentSpritesheet;

        spriteColor = new Paint();
        spriteColor.setColorFilter(new PorterDuffColorFilter(Color.WHITE, MULTIPLY));
    }

    public void setCurrentSpritesheet(Spritesheet sprite) { currentSpritesheet = sprite;}
    public void setAnim(int anim) {
        currentAnim = anim;
    }
    public void setStep(int step) { currentStep = step;}

    public void updateColorFilter(int currentHealth, int maxHealth) {
        int greenAndBlue = (int)(255 * (float) currentHealth /maxHealth);
        int newColor = Color.argb(255, 255, greenAndBlue, greenAndBlue);
        spriteColor.setColorFilter(new PorterDuffColorFilter(newColor, MULTIPLY));
    }

    @Override
    public void draw(Canvas canvas) {
        if (posComp == null) {
            posComp = (PositionComponent) owner.getComponent(POSITION);
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
                posComp.posX, posComp.posY,
                characterScaleFactor,
                spriteColor
        );
    }
}
