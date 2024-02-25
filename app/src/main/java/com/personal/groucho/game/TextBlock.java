package com.personal.groucho.game;

import com.personal.groucho.game.gameobjects.Resettable;

import java.util.ArrayList;

public class TextBlock implements Resettable {
    private final ArrayList<String> sentences = new ArrayList<>();

    public void add(String sentence) {
        sentences.add(sentence);
    }

    public boolean isFull() {
        return sentences.size() == 3;
    }

    public ArrayList<String> getSentences() {return sentences;}

    @Override
    public void reset() {
        sentences.clear();
    }
}
