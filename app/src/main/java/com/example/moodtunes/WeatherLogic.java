package com.example.moodtunes;

public interface WeatherLogic {
    void onWeatherSuccess(String mainWeather, String weatherDescription);

    void onWeatherFailure(String errorMessage);
}