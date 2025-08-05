package com.example.moodtunes;

import com.google.gson.annotations.SerializedName;

public class IpinfoResponse {
    @SerializedName("city")
    private String city;

    public String getCity() {
        return city;
    }
}