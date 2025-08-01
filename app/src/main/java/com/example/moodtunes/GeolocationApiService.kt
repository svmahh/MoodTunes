package com.example.moodtunes

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeolocationApiService(private val callback: GeolocationLogic) {
    private val service: GeolocationService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ip-api.com/json/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(GeolocationService::class.java)
    }

    fun fetchGeolocation() {
        val call = service.getGeolocation()
        call.enqueue(object : Callback<GeolocationResponse> {
            override fun onResponse(call: Call<GeolocationResponse>, response: Response<GeolocationResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val city = response.body()!!.city
                    callback.onGeolocationSuccess(city)
                } else {
                    callback.onGeolocationFailure("API call failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GeolocationResponse>, t: Throwable) {
                // Pass the actual error message
                callback.onGeolocationFailure(t.message)
            }
        })
    }
}