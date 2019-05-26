package com.moshenskyi.bullsheepandroid.pref;

import android.content.SharedPreferences;

import com.moshenskyi.bullsheepandroid.App;

import static android.content.Context.MODE_PRIVATE;

public class UserPrefManager {

    private static final String MY_PREFS = "MyPrefs";

    private static final String NAME_KEY = "name";
    private static final String SCORE_KEY = "score";
    private static final String HEALTH_KEY = "health";
    private static final String MOOD_KEY = "mood";

    private static UserPrefManager userPrefManager = new UserPrefManager();

    public static UserPrefManager getInstance() {
        return userPrefManager;
    }

    public void writeName(String name) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putString(NAME_KEY, name);
        editor.apply();
    }

    public String getName() {
        SharedPreferences prefs = App.getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        return prefs.getString(NAME_KEY, null);
    }

    public void writeHealthPoint(int healthP) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putInt(HEALTH_KEY, healthP);
        editor.apply();
    }

    public void writeMoodPoint(int moodP) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
        editor.putInt(MOOD_KEY, moodP);
        editor.apply();
    }

    public int getHealthPoint() {
        SharedPreferences prefs = App.getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        return prefs.getInt(HEALTH_KEY, 220);
    }

    public int getMoodPoint() {
        SharedPreferences prefs = App.getContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);
        return prefs.getInt(MOOD_KEY, -30);
    }
}
