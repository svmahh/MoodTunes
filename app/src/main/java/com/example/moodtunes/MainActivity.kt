package com.example.moodtunes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 * MainActivity is the main entry point of the application.
 * It uses geolocation to fetch weather data and retrieves song recommendations
 * from Last.fm based on the current weather conditions.
 *
 * Implements:
 * - WeatherLogic: to handle weather data callbacks
 * - GeolocationLogic: to handle location fetching
 * - LastfmLogic: to handle Last.fm API callbacks
 */
class MainActivity : AppCompatActivity(), WeatherLogic, GeolocationLogic, LastfmLogic {
    private lateinit var weatherService: WeatherService
    private lateinit var geolocationApiService: GeolocationApiService
    private lateinit var lastfmApiService: LastfmApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherService = WeatherService(this)
        geolocationApiService = GeolocationApiService(this)
        lastfmApiService = LastfmApiService(this)

        // Request location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            geolocationApiService.fetchGeolocation()
        }

        findViewById<Button>(R.id.btnSongs).setOnClickListener {

            // make methods to display songs and view here
            Intent(this, MoodActivity::class.java).also { intent ->
                intent.putExtra("MOOD_TAG", TAG.lowercase())
                startActivity(intent)


            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                geolocationApiService.fetchGeolocation()
            } else {
                onGeolocationFailure("Location permission denied")
            }
        }
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

    override fun onWeatherSuccess(mainWeather: String, weatherDescription: String) {
        Log.d(TAG, "Weather: $mainWeather")
        Log.d(TAG, "Description: $weatherDescription")
        getSongRecommendations(mainWeather)
    }

    override fun onWeatherFailure(errorMessage: String) {
        Log.e(TAG, "Failed to get weather data: $errorMessage")
    }

    private fun getSongRecommendations(weather: String) {
        val tag = when (weather.lowercase()) {
            "clear" -> "pop"
            "clouds" -> "indie"
            "rain", "drizzle" -> "acoustic"
            "thunderstorm" -> "rock"
            "snow" -> "folk"
            else -> "pop"
        }
        lastfmApiService.fetchTopTracks(tag)
    }

    override fun onRecommendationsSuccess(recommendations: LastfmResponse) {
        Log.d(TAG, "--- SONG RECOMMENDATIONS ---")
        recommendations.tracks.trackList.forEach { track ->
            Log.d(TAG, "'${track.name}' by ${track.artist.name}")
        }
    }

    override fun onRecommendationsFailure(errorMessage: String?) {
        Log.e(TAG, "Last.fm Error: $errorMessage")
    }

    companion object {
        private const val TAG = "WeatherApp"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}