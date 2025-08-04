package com.example.moodtunes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastfmService {
    @GET("2.0/")
    Call<LastfmResponse> getTopTracksByTag(
            @Query("method") String method,
            @Query("tag") String tag,
            @Query("api_key") String apiKey,
            @Query("format") String format
    );
}