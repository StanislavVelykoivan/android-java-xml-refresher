package com.velykoivanstanislav.animals.di;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application app;
    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    public Application provideApp() {
        return app;
    }
}
