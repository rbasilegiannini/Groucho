package com.personal.groucho.game;

import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterScaleFactor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.personal.groucho.badlogic.androidgames.framework.Input;

import java.util.ArrayList;
import java.util.List;

public class BubbleSpeech {
    private final GameWorld gameWorld;
    private final TextPaint paint;
    private final Rect src, dest;
    private int posX, posY, width, height;
    private Bitmap bubble;
    private final StringBuilder currentLine = new StringBuilder();
    private TextBlock currentTextBlock;
    private final List<TextBlock> textBlocks = new ArrayList<>();
    private final ObjectsPool<TextBlock> textBlocksPool= new ObjectsPool<>(10, TextBlock.class);

    public BubbleSpeech(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
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
            dest.left = (int) (posX - 0.50 * width);
            dest.top = (int) (posY - 0.70 * height);
            dest.right = (int) (posX + 0.60*width);
            dest.bottom = (int) (posY + 0.35 * height);

            canvas.drawBitmap(bubble, src, dest, paint);

            float x = dest.left + 50;
            float y = dest.top + paint.getTextSize() + 10;

            for (String line : textBlocks.get(0).getSentences()) {
                canvas.drawText(line, x, y, paint);
                int lineSpacing = 10;
                y += paint.getTextSize() + lineSpacing;
            }
        }
        else {
            gameWorld.grouchoIsTalking = false;
        }
    }

    public void setText(String text) {
        String[] words = text.split(" ");
        currentLine.setLength(0);
        currentTextBlock = textBlocksPool.acquire();

        for (String word : words) {
            processWord(word);
        }

        if (currentLine.length() > 0) {
            completeCurrentTextBlock();
        }
    }

    private void processWord(String word) {
        if (word.equals("\n")) {
            completeCurrentTextBlock();
            currentTextBlock = textBlocksPool.acquire();
        }
        else {
            if (lineIsNotTooLarge(word)) {
                updateCurrentLine(word);
            }
            else {
                updateCurrentTextBlock(word);
            }
        }
    }

    private void completeCurrentTextBlock() {
        addLineToTextBlock(currentTextBlock);
        textBlocks.add(currentTextBlock);
    }

    private void updateCurrentTextBlock(String word) {
        addLineToTextBlock(currentTextBlock);
        currentLine.append(word);

        if (currentTextBlock.isFull()) {
            textBlocks.add(currentTextBlock);
            currentTextBlock = textBlocksPool.acquire();
        }
    }

    private void updateCurrentLine(String word) {
        if (currentLine.length() > 0) {
            currentLine.append(" ");
        }
        currentLine.append(word);
    }

    private boolean lineIsNotTooLarge(String word) {
        return (currentLine.length() + word.length()) * paint.getTextSize() <= 1.75 * width;
    }

    private void addLineToTextBlock(TextBlock currentTextBlock) {
        currentTextBlock.add(currentLine.toString());
        currentLine.setLength(0);
    }

    public void setPosX(int posX){ this.posX = (int) (posX - width/2 - 0.65*(characterScaleFactor*characterDimX));}
    public void setPosY(int posY){ this.posY = posY + height;}

    public void consumeTouchEvent(Input.TouchEvent event) {
        if (event.type == Input.TouchEvent.TOUCH_DOWN) {
            textBlocksPool.release(textBlocks.get(0));
            textBlocks.remove(0);
        }
    }
}
