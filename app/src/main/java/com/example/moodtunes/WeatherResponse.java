package com.example.moodtunes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("weather")
    private List<Weather> weather;

    public List<Weather> getWeather() {
        return weather;
    }

    public static class Weather {
        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }
    }
}