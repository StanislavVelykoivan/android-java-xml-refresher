package com.velykoivanstanislav.animals;

import android.app.Application;

import com.velykoivanstanislav.animals.di.PrefsModule;
import com.velykoivanstanislav.animals.util.SharedPreferencesHelper;

public class PrefsModuleTest extends PrefsModule {

    SharedPreferencesHelper mockPrefs;
    public PrefsModuleTest(SharedPreferencesHelper mockPrefs) {
        this.mockPrefs = mockPrefs;

    }

    @Override
    public SharedPreferencesHelper providesSharedPreferences(Application app) {
        return mockPrefs;
    }


}
