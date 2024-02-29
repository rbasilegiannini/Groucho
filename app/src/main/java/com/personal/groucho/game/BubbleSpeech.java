package com.personal.groucho.game;

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
    private int posX, posY, width, height, offsetX, offsetY;
    private Bitmap bubble;
    private final StringBuilder currentLine = new StringBuilder();
    private TextBlock currentTextBlock;
    private final List<TextBlock> textBlocks = new ArrayList<>();

    public BubbleSpeech(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
        paint.setTypeface(Typeface.create("comic_sans", Typeface.NORMAL));

        offsetX = 0;
        offsetY = 0;
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
                canvas.drawText(line, offsetX + x, offsetY + y, paint);
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
        currentTextBlock = Pools.textBlocksPool.acquire();

        for (String word : words) {
            processWord(word);
        }

        if (currentLine.length() > 0) {
            completeCurrentTextBlock();
        }
    }

    public void setLeftAlignment() {
        offsetX = 0;
        offsetY = 0;
    }

    public void setCenterAlignment(){
        offsetX = (int) (0.25*width);
        offsetY = (int) (0.35*height);
    }

    public void setNormalText() {
        paint.setTypeface(Typeface.create("serif", Typeface.NORMAL));
    }

    public void setBoldText() {
        paint.setTypeface(Typeface.create("serif", Typeface.BOLD));
    }

    private void processWord(String word) {
        if (word.equals("\n")) {
            completeCurrentTextBlock();
            currentTextBlock = Pools.textBlocksPool.acquire();
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
            currentTextBlock = Pools.textBlocksPool.acquire();
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

    public void setPosX(int posX){ this.posX = posX - width/2;}
    public void setPosY(int posY){ this.posY = posY - height;}

    public void consumeTouchEvent(Input.TouchEvent event) {
        if (event.type == Input.TouchEvent.TOUCH_DOWN) {
            Pools.textBlocksPool.release(textBlocks.get(0));
            textBlocks.remove(0);
        }
    }
}
