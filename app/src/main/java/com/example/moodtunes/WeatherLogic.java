package com.example.moodtunes;

import com.example.moodtunes.WeatherResponse;

public interface WeatherLogic {
    void onWeatherSuccess(String mainWeather, String weatherDescription);
    void onWeatherFailure(String errorMessage);
}