package com.example.ravi.Quizz;

import android.graphics.drawable.Drawable;

import java.util.HashMap;
import java.util.Map;

public class Answer {

    private final String title;
    private Drawable background;
    public boolean isSelected;

    private Map<String, String> titleToCodeMap = new HashMap<String, String>() {
        {
            put("Aéroport", "AIRPORT");
            put("Parc d'attraction", "AMUSEMENT_PARK");
            put("Aquarium", "AQUARIUM");
            put("Gallerie d'art", "ART_GALLERY");
            put("Bar", "BAR");
            put("Bowling", "BOWLING_ALLEY");
            put("Café", "CAFE");
            put("Camping", "CAMPGROUND");
            put("Casino", "CASINO");
            put("Église", "CHURCH");
            put("Temple hindou", "HINDU_TEMPLE");
            put("Discothèque", "NIGHT_CLUB");
            put("Musée", "MUSEUM");
            put("Cinéma", "MOVIE_THEATER");
            put("Mosquée", "MOSQUE");
            put("Parc", "PARK");
            put("Restaurant", "RESTAURANT");
            put("Centre commercial", "SHOPPING_MALL");
            put("Stade", "STADIUM");
            put("Synagogue", "SYNAGOGUE");
            put("Zoo", "ZOO");
        }
    };

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

    public String getTitleCode() {
        return titleToCodeMap.get(title);
    }

    public Drawable getBackground() {
        return background;
    }

    public boolean select() {
        isSelected = !isSelected;
        return isSelected;
    }
}
