package com.personal.groucho.game;

import static com.personal.groucho.game.Graphics.bufferHeight;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterDimY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.personal.groucho.badlogic.androidgames.framework.Input;
import com.personal.groucho.game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BubbleSpeech {
    private final GameWorld gameWorld;
    private final TextPaint paint;
    private final int lineSpacing = 10;
    private final Rect src, dest;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private Bitmap bubble;

    public BubbleSpeech(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTypeface(Typeface.create("serif", Typeface.NORMAL));

        src = new Rect();
        dest = new Rect();
    }

    public void setBubbleTexture(Bitmap texture) {
        bubble = texture;
        width = bubble.getWidth();
        height = bubble.getHeight();

        src.top = 0;
        src.left = 0;
        src.bottom = bubble.getHeight();
        src.right = bubble.getWidth();
    }

    public void draw(Canvas canvas) {
        if (!textBlocks.isEmpty()) {
            dest.left = posX - width / 2;
            dest.top = (int) (posY - 0.75 * height);
            dest.right = posX + width / 2;
            dest.bottom = (int) (posY + 0.25 * height);

            canvas.drawBitmap(bubble, src, dest, paint);

            float x = dest.left + 50;
            float y = dest.top + paint.getTextSize();

            for (String line : textBlocks.get(0).getSentences()) {
                canvas.drawText(line, x, y, paint);
                y += paint.getTextSize() + lineSpacing;
            }
        }
        else {
            gameWorld.grouchoIsTalking = false;
        }
    }

    // TODO: will be a pool
    List<TextBlock> textBlocks = new ArrayList<>();
    public void setText(String text) {
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        TextBlock currentTextBlock = new TextBlock();
        for (String word : words) {
            if ((currentLine.length() + word.length()) * paint.getTextSize() <= 2*width) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                currentTextBlock.add(currentLine.toString());
                currentLine = new StringBuilder(word);

                if (currentTextBlock.isFull()) {
                    textBlocks.add(currentTextBlock);
                    currentTextBlock = new TextBlock();
                }
            }
        }

        if (currentLine.length() > 0) {
            currentTextBlock.add(currentLine.toString());
            textBlocks.add(currentTextBlock);
        }
    }

    public void setPosX(int posX){ this.posX = (int) (posX - width/2 - 0.65*(characterScaleFactor*characterDimX));}
    public void setPosY(int posY){ this.posY = posY + height;}

    public void consumeTouchEvent(Input.TouchEvent event) {
        if (event.type == Input.TouchEvent.TOUCH_DOWN) {
            textBlocks.remove(0);
        }
    }
}
