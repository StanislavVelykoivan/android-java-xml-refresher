package com.velykoivanstanislav.animals.model;



import com.velykoivanstanislav.animals.di.DaggerApiComponent;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class AnimalApiService {

    @Inject
    AnimalApi api;

    public AnimalApiService(){
        DaggerApiComponent.create().inject(this);
    }

    public Single<ApiKeyModel> getApiKey() {
        return api.getApiKey();
    }

    public Single<List<AnimalModel>> getAnimals(String key) {
        return api.getAnimals(key);
    }
}
