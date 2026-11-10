package com.velykoivanstanislav.animals;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.velykoivanstanislav.animals.di.AppModule;
import com.velykoivanstanislav.animals.di.DaggerViewModelComponent;
import com.velykoivanstanislav.animals.model.AnimalApi;
import com.velykoivanstanislav.animals.model.AnimalApiService;
import com.velykoivanstanislav.animals.model.AnimalModel;
import com.velykoivanstanislav.animals.model.ApiKeyModel;
import com.velykoivanstanislav.animals.util.SharedPreferencesHelper;
import com.velykoivanstanislav.animals.viewmodel.ListViewModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;


public class ListViewModelTest {
    private String key = "Test key";

    private AutoCloseable mocks;
    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    AnimalApiService animalApiService;

    @Mock
    SharedPreferencesHelper prefs;

    Application application = Mockito.mock(Application.class);

    ListViewModel listViewModel = new ListViewModel(application, true);

    @Before
    public void setup(){
        mocks = MockitoAnnotations.openMocks(this);

        DaggerViewModelComponent.builder()
                .appModule(new AppModule(application))
                .apiModule(new ApiModuleTest(animalApiService))
                .prefsModule(new PrefsModuleTest(prefs))
                .build()
                .inject(listViewModel);
    }

    @Test
    public void getAnimalsSuccess(){
        Mockito.when(prefs.getApiKey()).thenReturn(key);
        AnimalModel animal = new AnimalModel("cow", null, null, null, null, null, null);
        ArrayList<AnimalModel> list = new ArrayList<AnimalModel>();
        list.add(animal);

        Single testSingle = Single.just(list);

        Mockito.when(animalApiService.getAnimals(key)).thenReturn(testSingle);

        listViewModel.refresh();

        Assert.assertEquals(1, listViewModel.animals.getValue().size());
        Assert.assertEquals(false, listViewModel.loadError.getValue());
        Assert.assertEquals(false, listViewModel.loading.getValue());

    }

    @Test
    public void getAnimalsFailure() {
        Mockito.when(prefs.getApiKey()).thenReturn(key);
        Single testSingle = Single.error(new Throwable());
        Single keySingle = Single.just(new ApiKeyModel("OK", key));

        Mockito.when(animalApiService.getAnimals(key)).thenReturn(testSingle);
        Mockito.when(animalApiService.getApiKey()).thenReturn(keySingle);

        listViewModel.refresh();

        Assert.assertEquals(null, listViewModel.animals.getValue());
        Assert.assertEquals(false, listViewModel.loading.getValue());
        Assert.assertEquals(true, listViewModel.loadError.getValue());
    }


    @After
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Before
    public void setupRxSchedulers(){
        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(runnable -> {
                    runnable.run();
                }, true);
            }
        };
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }
}
