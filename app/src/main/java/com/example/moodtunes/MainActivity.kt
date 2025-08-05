package com.example.moodtunes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity is the main entry point of the application.
 * It fetches weather data for a specific city and then retrieves song recommendations
 * from Last.fm based on the current weather conditions.
 *
 * This activity implements [WeatherLogic] to handle weather data callbacks and
 * [LastfmLogic] to handle Last.fm API callbacks.
 */

class MainActivity : AppCompatActivity(), WeatherLogic, LastfmLogic {
    private lateinit var weatherService: WeatherService
    private lateinit var lastfmApiService: LastfmApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherService = WeatherService(this)
        lastfmApiService = LastfmApiService(this)

        // Directly fetch weather for a hardcoded city, no geolocation needed.
        weatherService.fetchWeatherData("Port Elizabeth")

        findViewById<Button>(R.id.btnSongs).setOnClickListener {

            // make methods to display songs and view here
            Intent(this, MoodActivity::class.java).also { intent ->
                intent.putExtra("MOOD_TAG", TAG.lowercase())
                startActivity(intent)


            }
        }

    }

    override fun onRecommendationsSuccess(recommendations: LastfmResponse) {
        // Get the list of LastfmResponse.Track objects
        val tracks = recommendations
            .getTracks()
            .getTrackList()
            .filterNotNull()
       // adapter.updateTracks(tracks)
    }

    override fun onRecommendationsFailure(errorMessage: String?) {
        Log.e("MoodActivity", "Last.fm Error: $errorMessage")
    }


    override fun onWeatherSuccess(mainWeather: String, weatherDescription: String) {
        Log.d(TAG, "Weather for Gqeberha: $mainWeather")
        Log.d(TAG, "Description: $weatherDescription")
        getSongRecommendations(mainWeather)
    }

    private fun getSongRecommendations(weather: String) {
        // Simple logic to map weather to a Last.fm tag
        val tag = when (weather.lowercase()) {
            "clear" -> "pop"
            "clouds" -> "indie"
            "rain", "drizzle" -> "acoustic"
            "thunderstorm" -> "rock"
            "snow" -> "folk"
            else -> "pop" // Default tag
        }
        lastfmApiService.fetchTopTracks(tag)
    }

    override fun onWeatherFailure(errorMessage: String) {
        Log.e(TAG, "Failed to get weather data: $errorMessage")
    }

    /*override fun onRecommendationsSuccess(recommendations: LastfmResponse) {
        Log.d(TAG, "--- SONG RECOMMENDATIONS ---")
        recommendations.tracks.trackList.forEach { track ->
            Log.d(TAG, "'${track.name}' by ${track.artist.name}")
        }
    }*/

    /*override fun onRecommendationsFailure(errorMessage: String?) {
        Log.e(TAG, "Last.fm Error: $errorMessage")
    }*/

    companion object {
        private const val TAG = "WeatherApp"
    }
}