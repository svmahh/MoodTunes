package com.example.moodtunes

import retrofit2.Call
import retrofit2.http.GET

interface GeolocationService {
    @GET(".") // Or your specific endpoint path if not the base URL
    fun getGeolocation(): Call<GeolocationResponse>
}