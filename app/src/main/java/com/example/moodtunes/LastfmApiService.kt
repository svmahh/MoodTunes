package com.example.moodtunes

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LastfmApiService(private val callback: LastfmLogic) {
    private val apiKey = "cb1f261cb862e5b89fb02d5735fd0be7"
    private val service: LastfmService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://ws.audioscrobbler.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(LastfmService::class.java)
    }

    fun fetchTopTracks(tag: String) {
        val call = service.getTopTracksByTag("tag.gettoptracks", tag, apiKey, "json")
        call.enqueue(object : Callback<LastfmResponse> {
            override fun onResponse(call: Call<LastfmResponse>, response: Response<LastfmResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onRecommendationsSuccess(response.body()!!)
                } else {
                    callback.onRecommendationsFailure("Failed to get recommendations: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LastfmResponse>, t: Throwable) {
                callback.onRecommendationsFailure("Failed to get recommendations: ${t.message}")
            }
        })
    }
}