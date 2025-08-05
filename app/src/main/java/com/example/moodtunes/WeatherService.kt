package com.example.moodtunes

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService(callback: WeatherLogic) {
    private val service: OpenWeatherMapService
    private val callback: WeatherLogic = callback

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        this.service = retrofit.create(OpenWeatherMapService::class.java)
    }

    fun fetchWeatherData(cityName: String?) { //fetches weather data
        val call = service.getCurrentWeather(cityName, API_KEY) //api call
        call.enqueue(object : Callback<WeatherResponse?> {
            override fun onResponse(
                call: Call<WeatherResponse?>,
                response: Response<WeatherResponse?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val weatherResponse = response.body()

                    if (weatherResponse!!.weather != null && !weatherResponse.weather.isEmpty()) { //checks if weather is not empty
                        val mainWeather = weatherResponse.weather[0].main
                        val weatherDescription = weatherResponse.weather[0].description

                        // Use the callback to send success data back to the activity
                        callback.onWeatherSuccess(mainWeather, weatherDescription)
                    } else {
                        callback.onWeatherFailure("Weather data not found in response.") //if weather is empty
                    }
                } else {
                    try {
                        val errorBody = if (response.errorBody() != null) response.errorBody()!!
                            .string() else "Unknown error"
                        callback.onWeatherFailure("API call failed. Response code: " + response.code() + ", Error: " + errorBody) //if api call fails
                    } catch (e: Exception) {
                        callback.onWeatherFailure("API call failed. Response code: " + response.code()) //if api call fails
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse?>, t: Throwable) {
                // Use the callback to send the failure message
                callback.onWeatherFailure("API call failed: " + t.message)
            }
        })
    }
    /* ⢀⡀⢠⠠⣀⠤⠠⡄⡄⠠⢄⠤⡅⢌⣀⡉⣈⢈⣀⣙⢊⡑⣂⣉⢂⣐⣂⠐⡐⠀⠂⠀⠀
     ⠤⢌⠑⢆⣠⠖⡩⠔⡡⠐⠤⣚⠥⣓⣜⠓⠊⠉⠈⠚⣆⢪⣎⢛⡼⢵⡻⣁⠔⡩⢄⠋⠦
     ⠘⢤⠋⣆⠳⡬⠡⢜⠀⠀⡀⠀⠀⠀⠀⢀⣀⣀⢀⡀⠀⠉⠀⠉⠈⠀⠁⢉⢚⠰⢈⠅⢣
     ⢊⣆⢓⣌⢳⡁⢏⠆⠀⠐⠀⠀⠀⢠⣼⣿⣿⣿⣿⣿⣶⡀⠀⠀⠀⠀⠀⠀⠄⢰⠃⠌⠤
     ⢆⣉⡎⡰⢇⢎⠱⠀⠀⠀⢀⠀⠀⡿⠿⠿⣿⣿⣿⡿⠿⣷⠀⠀⠀⠀⠀⠀⠀⠀⢸⠸⣀
     ⠼⡐⠍⢇⡩⢊⠁⠁⠀⠀⡂⠀⢠⠔⠂⠀⢀⢭⡟⠀⠀⡐⡄⠀⠀⠀⠀⠀⠀⠁⠐⠐⡄
     ⠣⡄⢏⢪⡄⢃⠀⡌⠀⠐⠀⢂⣿⣶⣶⣶⢮⢼⣿⣵⣶⣾⣧⠀⠀⠀⠀⠀⠀⠀⠀⠰⣞
     ⡱⠵⡊⢶⠉⠀⠠⠀⢀⠁⠀⣼⣿⣿⣿⡟⢘⣻⣿⣿⣿⣿⣿⡐⠔⡠⠀⠀⠘⡀⠄⠁⢹
     ⡞⣩⠟⠂⠈⠀⠀⠀⡈⠀⠘⢺⣿⣿⣿⣅⠁⠛⢿⢛⣿⣿⣿⠀⠒⡠⠀⠀⠀⠰⠀⠅⠂
     ⣎⠵⠊⠀⠀⠀⡀⠀⠀⠀⠀⣇⡿⡽⣿⣿⣯⣿⣿⣿⣿⣿⣿⠀⠁⡀⠀⠀⠀⠀⠠⠐⠁
     ⠈⠎⠀⠀⠈⠀⠀⠀⠀⠀⠀⠲⣿⣶⣄⢀⡉⠉⠉⡉⣛⣿⡏⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
     ⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣟⣷⣮⣷⣭⣹⣿⣿⣿⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
     ⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⢂⢽⣹⢿⣿⣾⣿⣿⣿⣿⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
     ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⢹⡀⢏⡿⣾⣿⣿⣿⣿⣿⣿⡀⠰⠀⠀⠀⠀⠀⠀⠀⠀⠀
     ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣶⢫⠷⡌⢿⣿⣿⣿⣿⣿⣿⣿⠈⡑⠠⠐⢀⠀⠀⠀⠀⠀⠀
     ⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⡈⡻⢵⡀⣈⣙⣉⣯⡽⣳⠃⡐⢠⠡⠀⠌⡌⠂⠄⠀⠀⠀
     ⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⣷⡘⢣⠒⣄⢺⡽⢫⣾⠏⡀⠔⠂⡐⠀⢸⠐⡩⢌⠰⡀⠀
     ⠀⠀⠀⠀⠀⢀⠀⠀⠀⣿⣿⣿⣿⣄⠣⠄⣻⣴⣿⡟⠀⡀⠂⠡⠀⠀⠰⣉⠖⡨⠱⢊⠔*/
// api key in main :0
// put api key in gradle file ( im jk ik it doesnt really matter here but hmmm )
    companion object {
        private const val TAG = "WeatherService"
        private const val BASE_URL = "https://api.openweathermap.org/" //api url
        private const val API_KEY = "39582bb680d2cc4aa0907c6c71fdfff3" //api key
    }
}