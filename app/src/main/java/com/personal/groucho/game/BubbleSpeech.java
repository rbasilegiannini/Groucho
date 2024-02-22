package com.personal.groucho.game;

import static com.personal.groucho.game.Graphics.bufferHeight;
import static com.personal.groucho.game.Graphics.bufferWidth;
import static com.personal.groucho.game.assets.Textures.bubble;
import static com.personal.groucho.game.constants.System.characterDimX;
import static com.personal.groucho.game.constants.System.characterDimY;
import static com.personal.groucho.game.constants.System.characterScaleFactor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;

import java.util.ArrayList;

public class BubbleSpeech {
    private final TextPaint paint;
    private final int lineSpacing = 10;
    private final Rect src, dest;
    private ArrayList<String> lines = new ArrayList<>();
    private int posX;
    private int posY;
    private final int width;
    private int height;

    public BubbleSpeech() {
        width = bubble.getWidth();
        height = bubble.getHeight();

        posX = bufferWidth/2 - width/2;
        posY = (int) (bufferHeight/2 + height/2 + characterScaleFactor * characterDimY);
        paint = new TextPaint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
        paint.setTypeface(Typeface.DEFAULT);

        src = new Rect(0, 0, bubble.getWidth(), bubble.getHeight());
        dest = new Rect(posX, posY, posX + width, posY + height);
    }

    public void draw(Canvas canvas) {
        dest.left = posX;
        dest.top = posY;
        dest.right = posX + width;
        dest.bottom = posY + height;

        canvas.drawBitmap(bubble, src, dest, paint);

        float x = posX + 60;
        float y = posY + height/2;
        for (String line : lines) {
            canvas.drawText(line, x, y, paint);
            y += paint.getTextSize() + lineSpacing;
        }
    }

    public void setText(String text) {
        lines.clear();
        StringBuilder currentLine = new StringBuilder();

        String[] words = text.split("\\s+");
        for (String word : words) {
            if ((currentLine.length() + word.length()) * paint.getTextSize() <= 2*width) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
    }

    public void setPosX(int posX){ this.posX = (int) (posX - width/2 - 0.65*(characterScaleFactor*characterDimX));}
    public void setPosY(int posY){ this.posY = posY + height;}

}
