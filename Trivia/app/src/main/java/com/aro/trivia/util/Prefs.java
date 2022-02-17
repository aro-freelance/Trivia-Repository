package com.aro.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    public static final String HIGH_SCORE = "highscore";
    private final SharedPreferences preferences;

    public Prefs(Activity context){
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score, String category){
        int currentScore = score;
        int highScore = preferences.getInt(HIGH_SCORE + "_" + category, 0);




        if(currentScore > highScore){
            preferences.edit().putInt(HIGH_SCORE + "_" + category, currentScore).apply();
        }
    }

    public int getHighScore(String category){
        return preferences.getInt(HIGH_SCORE + "_" + category, 0);
    }


}
