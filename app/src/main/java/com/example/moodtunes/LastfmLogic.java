package com.example.moodtunes;

public interface LastfmLogic {
    void onRecommendationsSuccess(LastfmResponse recommendations);
    void onRecommendationsFailure(String errorMessage);
}