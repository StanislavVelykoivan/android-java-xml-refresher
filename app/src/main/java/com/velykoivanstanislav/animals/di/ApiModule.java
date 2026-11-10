package com.velykoivanstanislav.animals.di;

import com.velykoivanstanislav.animals.BuildConfig;
import com.velykoivanstanislav.animals.model.AnimalApi;
import com.velykoivanstanislav.animals.model.AnimalApiService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {
    @Provides
    public AnimalApi provideAnimalApi() {
        return new Retrofit.Builder()
            .baseUrl(BuildConfig.ANIMALS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AnimalApi.class);
    }

    @Provides
    public AnimalApiService provideAnimalApiService() {
        return new AnimalApiService();
    }
}
