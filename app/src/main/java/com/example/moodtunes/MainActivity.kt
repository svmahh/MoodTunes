package com.example.moodtunes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), WeatherLogic {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val weatherService = WeatherService(this)


        weatherService.fetchWeatherData(CITY_NAME)
    }


    override fun onWeatherSuccess(mainWeather: String, weatherDescription: String) {

        Log.d(TAG, "Weather for " + CITY_NAME + ": " + mainWeather)
        Log.d(TAG, "Description: $weatherDescription")


    }

    override fun onWeatherFailure(errorMessage: String) {

        Log.e(TAG, "Failed to get weather data: $errorMessage")


    }

    companion object {
        private const val TAG = "WeatherApp"
        private const val CITY_NAME = "Port Elizabeth"
    }
}