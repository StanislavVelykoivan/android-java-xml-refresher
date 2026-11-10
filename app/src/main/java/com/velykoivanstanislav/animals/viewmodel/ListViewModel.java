package com.velykoivanstanislav.animals.viewmodel;

import static com.velykoivanstanislav.animals.di.TypeOfContext.CONTEXT_APP;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.velykoivanstanislav.animals.di.AppModule;
import com.velykoivanstanislav.animals.di.DaggerViewModelComponent;
import com.velykoivanstanislav.animals.di.TypeOfContext;
import com.velykoivanstanislav.animals.model.AnimalApiService;
import com.velykoivanstanislav.animals.model.AnimalModel;
import com.velykoivanstanislav.animals.model.ApiKeyModel;
import com.velykoivanstanislav.animals.util.SharedPreferencesHelper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends AndroidViewModel {

    @Inject
    AnimalApiService apiService;


    private CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<List<AnimalModel>> animals = new MutableLiveData<List<AnimalModel>>();
    public MutableLiveData<Boolean> loadError = new MutableLiveData<Boolean>();
    public MutableLiveData<Boolean> loading = new MutableLiveData<Boolean>();

    @Inject
            @TypeOfContext(CONTEXT_APP)
    SharedPreferencesHelper prefs;
    private Boolean invalidApiKey = false;
    private Boolean injected = false;



    private void inject() {
        if(!injected) {
            DaggerViewModelComponent.builder()
                    .appModule(new AppModule(getApplication()))
                    .build()
                    .inject(this);
        }
    }

    public void hardRefresh() {
        loading.setValue(true);
        getKey();
    }

    public ListViewModel(Application application) {
        super(application);
    }
    public ListViewModel(Application application, Boolean isTest) {
        super(application);
        injected = true;
    }

    public void refresh() {
        inject();
        loading.setValue(true);
        loadError.setValue(false);
        invalidApiKey = false;
        String key = prefs.getApiKey();

        if (key != null && !key.isEmpty()) {
            getAnimals(key);
        } else {
            getKey();
        }
    }

    private void getKey() {
        disposable.add(
                apiService.getApiKey()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ApiKeyModel>() {
                                           @Override
                                           public void onSuccess(ApiKeyModel key) {
                                               if (key.key.isEmpty()) {
                                                   loadError.setValue(true);
                                                   loading.setValue(false);
                                               } else {
                                                   prefs.saveApiKey(key.key);
                                                   getAnimals(key.key);
                                               }
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               e.printStackTrace();
                                               loadError.setValue(true);
                                               loading.setValue(false);
                                           }
                                       }
                        )
        );
    }


    private void getAnimals(String key) {
        disposable.add(
                apiService.getAnimals(key)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<List<AnimalModel>>() {
                                           @Override
                                           public void onSuccess(List<AnimalModel> list) {
                                               loadError.setValue(false);
                                               animals.setValue(list);
                                               loading.setValue(false);
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                               if (!invalidApiKey) {
                                                   invalidApiKey = true;
                                                   getKey();
                                               } else {
                                                   e.printStackTrace();
                                                   loadError.setValue(true);
                                                   loading.setValue(false);
                                               }
                                           }
                                       }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
