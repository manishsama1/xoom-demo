package com.demo.xoom.xoomdemo.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manish on 8/11/2018.
 */

public class XoomResponse {
    @SerializedName("total_pages")
    private int pageCount;

    public int getPageCount() {
        return pageCount;
    }

    @SerializedName("items")
    private List<Country> countryList;

    private List<Country> getCountryList() {
        return countryList;
    }

    public List<Country> getActiveCountryList() {
        List<Country> activeCountryList = new ArrayList<>();

        for (Country country : getCountryList()) {
            if (country.isActive()) {
                activeCountryList.add(country);
            }
        }
        return activeCountryList;
    }
}
