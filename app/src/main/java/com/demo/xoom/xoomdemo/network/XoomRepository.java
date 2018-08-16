package com.demo.xoom.xoomdemo.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Manish on 8/11/2018.
 */

public class XoomRepository {
    private static XoomRepository xoomRepository;

    private XoomRepository() {
    }

    public synchronized static XoomRepository getInstance() {
        if (xoomRepository == null) {
            if (xoomRepository == null) {
                xoomRepository = new XoomRepository();
            }
        }
        return xoomRepository;
    }

    public LiveData<XoomResponse> getCountryList(int pageNumber) {
        ApiInterface apiService =
                RetrofitClient.getClient().create(ApiInterface.class);
        final MutableLiveData<XoomResponse> data = new MutableLiveData<>();
        Call<XoomResponse> call = apiService.getCountries(25, pageNumber);
        call.enqueue(new Callback<XoomResponse>() {
            @Override
            public void onResponse(Call<XoomResponse>call, Response<XoomResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<XoomResponse>call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
