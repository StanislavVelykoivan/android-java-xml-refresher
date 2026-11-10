package com.velykoivanstanislav.animals.di;

import static com.velykoivanstanislav.animals.di.TypeOfContext.CONTEXT_ACTIVITY;
import static com.velykoivanstanislav.animals.di.TypeOfContext.CONTEXT_APP;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.velykoivanstanislav.animals.util.SharedPreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PrefsModule {
    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_APP)
    public SharedPreferencesHelper providesSharedPreferences(Application app) {
        return new SharedPreferencesHelper(app);
    }

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_ACTIVITY)
    public SharedPreferencesHelper providesActivitySharedPreferences(AppCompatActivity activity) {
        return new SharedPreferencesHelper(activity);
    }
}
