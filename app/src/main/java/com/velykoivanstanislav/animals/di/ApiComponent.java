package com.velykoivanstanislav.animals.di;

import com.velykoivanstanislav.animals.model.AnimalApiService;

import dagger.Component;

@Component(modules = {ApiModule.class})
public interface ApiComponent {
    void inject(AnimalApiService service);
}
