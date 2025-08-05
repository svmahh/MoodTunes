package com.example.moodtunes

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Service class for fetching geolocation data from the ipinfo.io API.
 *
 * This class uses Retrofit to make network requests and Gson for JSON parsing.
 * It provides a method to fetch the current device's geolocation and communicates
 * the results (success or failure) back via a [GeolocationLogic] callback.
 *
 * @param callback An instance of [GeolocationLogic] to handle the API response.
 */
class GeolocationApiService(private val callback: GeolocationLogic) {
    private val service: IpinfoService

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(UserAgentInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ipinfo.io/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(IpinfoService::class.java)
    }

    fun fetchGeolocation() {
        val call = service.getGeolocation()
        call.enqueue(object : Callback<IpinfoResponse> {
            override fun onResponse(
                call: Call<IpinfoResponse>,
                response: Response<IpinfoResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val city = response.body()!!.city
                    callback.onGeolocationSuccess(city)
                } else {
                    callback.onGeolocationFailure("API call failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<IpinfoResponse>, t: Throwable) {
                callback.onGeolocationFailure(t.message)
            }
        })
    }
}