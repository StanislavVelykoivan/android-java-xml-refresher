package com.velykoivanstanislav.animals.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper {

    private static final String PREFS_NAME = "animals_prefs";
    private static final String PREF_API_KEY = "Api key";

    private SharedPreferences prefs;

    public SharedPreferencesHelper(Context context) {
        prefs = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
        );
    }

    public void saveApiKey(String apiKey) {
        prefs.edit().putString(PREF_API_KEY, apiKey).apply();
    }

    public String getApiKey() {
        return prefs.getString(PREF_API_KEY, null);
    }

}
