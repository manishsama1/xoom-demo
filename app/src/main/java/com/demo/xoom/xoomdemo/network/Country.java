package com.demo.xoom.xoomdemo.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Manish on 8/11/2018.
 */

public class Country {
    public static final String MODE_ACTIVE = "ACTIVE";

    @SerializedName("code")
    private String countryCode;

    @SerializedName("name")
    private String countryName;

    @SerializedName("disbursement_options")
    private List<DisbursementOption> disbursementOptions;

    private boolean isFavorite;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private List<DisbursementOption> getDisbursementOptions() {
        return disbursementOptions;
    }

    public boolean isActive() {
        if (getDisbursementOptions() == null || getDisbursementOptions().isEmpty()) {
            return false;
        }
        for (DisbursementOption option : getDisbursementOptions()) {
            if (option.getMode().equals(MODE_ACTIVE)) {
                return true;
            }
        }
        return false;
    }

    public class DisbursementOption {
        @SerializedName("mode")
        private String mode;

        public String getMode() {
            return mode;
        }
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }
}
