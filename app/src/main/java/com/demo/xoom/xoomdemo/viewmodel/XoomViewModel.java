package com.demo.xoom.xoomdemo.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.demo.xoom.xoomdemo.network.Country;
import com.demo.xoom.xoomdemo.network.XoomRepository;
import com.demo.xoom.xoomdemo.network.XoomResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 8/11/2018.
 */

public class XoomViewModel extends AndroidViewModel {
    private LiveData<XoomResponse> xoomLiveData;
    private int pageNumber;
    private List<Country> countryList;
    private int pageCount;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public XoomViewModel(@NonNull Application application) {
        super(application);
        countryList = new ArrayList<>();
    }

    public void getCountries() {
        xoomLiveData = XoomRepository.getInstance().getCountryList(pageNumber);
    }

    public List<Country> getCountryList() {
        return countryList;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public LiveData<XoomResponse> getObservableData() {
        return xoomLiveData;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private int pageNumber;

        public Factory(@NonNull Application application) {
            this.application = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new XoomViewModel(application);
        }
    }
}
