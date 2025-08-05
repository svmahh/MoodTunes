package com.example.moodtunes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import java.util.concurrent.Executors


class MoodActivity : AppCompatActivity() {

    private lateinit var ivMoodImage: ImageView
    private lateinit var rvTracks: RecyclerView
    private lateinit var adapter: TrackAdapter
    //private lateinit var outputTextView: TextView

    private val executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    // api shiii
    private  val UNSPLASH_BASE = "https://api.unsplash.com"
    private  val UNSPLASH_ACCESS_KEY = "GSaXne8REsDlky9_XB2S8rB0MzxEdPGwUCJM2XrqFCk"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mood_activity)

        ivMoodImage     = findViewById(R.id.ivMoodImage)
        rvTracks        = findViewById(R.id.rvTracks)

        adapter = TrackAdapter(emptyList())
        rvTracks.layoutManager = LinearLayoutManager(this)
        rvTracks.adapter       = adapter

        val tag = intent.getStringExtra("MOOD_TAG") ?: "pop"
        fetchUnsplashImage(tag)
        //fetchTopTracks(tag)
    }

    private fun fetchUnsplashImage(query: String) {
        val url = "https://api.unsplash.com/photos/random?query=$query"

        executor.execute {
            url
                .httpGet()
                .header("Authorization" to "Client-ID $UNSPLASH_ACCESS_KEY")
                .responseString { _, response, result ->
                    handler.post {
                        when (result) {
                            is Result.Success -> try {
                                val data = result.get()
                                val photo = Gson().fromJson(data, UnsplashResponse::class.java)
                                Glide.with(this)
                                    .load(photo.urls.regular)
                                    .into(ivMoodImage)

                            } catch (e: JsonSyntaxException) {
                                Log.e("MoodActivity", "Unsplash JSON error", e)
                            }

                            is Result.Failure -> {
                                Log.e(
                                    "MoodActivity",
                                    "Unsplash failed ${response.statusCode} ${response.responseMessage}",
                                    result.getException()
                                )
                            }
                        }
                    }
                }
        }
    }


   /* private fun fetchTopTracks(tag: String) {
        val lastfmService = LastfmApiService(this)
        lastfmService.fetchTopTracks(tag)
    }*/


    // --- Data models ---

    data class UnsplashResponse(val urls: Urls) {
        data class Urls(val regular: String)
    }

    data class LastfmResponse(val tracks: TracksContainer) {
        data class TracksContainer(
            @SerializedName("track")
            val trackList: List<LastfmTrack>
        )
    }

    data class LastfmTrack(
        val name: String,
        val artist: Artist
    )

    data class Artist(val name: String)
}