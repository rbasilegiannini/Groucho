package com.personal.groucho.game;

import java.util.ArrayList;

public class TextBlock {
    private final ArrayList<String> sentences = new ArrayList<>();

    public void add(String sentence) {
        sentences.add(sentence);
    }

    public boolean isFull() {
        return sentences.size() == 3;
    }

    public ArrayList<String> getSentences() {return sentences;}
}
