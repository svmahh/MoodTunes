package com.example.moodtunes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), WeatherLogic, GeolocationLogic {
    private lateinit var weatherService: WeatherService
    private lateinit var geolocationApiService: GeolocationApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherService = WeatherService(this)
        geolocationApiService = GeolocationApiService(this)

        // Request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            geolocationApiService.fetchGeolocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                geolocationApiService.fetchGeolocation()
            } else {
                onGeolocationFailure("Location permission denied")
            }
        }
    }

    override fun onWeatherSuccess(mainWeather: String, weatherDescription: String) {
        Log.d(TAG, "Weather: $mainWeather")
        Log.d(TAG, "Description: $weatherDescription")
    }

    override fun onWeatherFailure(errorMessage: String) {
        Log.e(TAG, "Failed to get weather data: $errorMessage")
    }

    override fun onGeolocationSuccess(city: String?) {
        if (city != null) {
            Log.d(TAG, "City: $city")
            weatherService.fetchWeatherData(city)
        } else {
            onGeolocationFailure("City not found")
        }
    }

    override fun onGeolocationFailure(errorMessage: String?) {
        Log.e(TAG, "Failed to get geolocation data: $errorMessage")
    }

    companion object {
        private const val TAG = "WeatherApp"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}