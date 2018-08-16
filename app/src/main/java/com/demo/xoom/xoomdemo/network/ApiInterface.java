package com.demo.xoom.xoomdemo.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Manish on 8/11/2018.
 */

public interface ApiInterface {
    @GET("https://mobile.xoom.com/catalog/v2/countries")
    Call<XoomResponse> getCountries(@Query("page_size") int pageSize,
                                    @Query("page") int page);
}
