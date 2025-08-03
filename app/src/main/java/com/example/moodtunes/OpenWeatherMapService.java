package com.example.moodtunes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("data/2.5/weather")
        //specifies end point for the api
    Call<WeatherResponse> getCurrentWeather(
            @Query("q") String cityName, //helps for city name
            @Query("appid") String apiKey //api key
    );
}