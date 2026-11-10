package com.velykoivanstanislav.animals;

import com.velykoivanstanislav.animals.di.ApiModule;
import com.velykoivanstanislav.animals.model.AnimalApiService;

public class ApiModuleTest extends ApiModule {
    AnimalApiService mockService;

    public ApiModuleTest(AnimalApiService mockService) {
        this.mockService = mockService;
    }

    @Override
    public AnimalApiService provideAnimalApiService() {
        return mockService;
    }
}
