package com.example.moodtunes

interface GeolocationLogic {
    fun onGeolocationSuccess(city: String?)
    fun onGeolocationFailure(errorMessage: String?)
}