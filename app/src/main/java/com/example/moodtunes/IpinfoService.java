package com.example.moodtunes;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IpinfoService {
    @GET("json")
    Call<IpinfoResponse> getGeolocation();
}