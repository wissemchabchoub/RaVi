package com.example.ravi.Quizz;

import android.graphics.drawable.Drawable;

public class Answer {

    private final String title;
    private Drawable background;
    public boolean isSelected;

    public Answer(String title) {
        this.title = title;
    }

    public Answer(String title, Drawable background) {
        this.title = title;
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getBackground() {
        return background;
    }

    public boolean select() {
        isSelected = !isSelected;
        return isSelected;
    }
}
