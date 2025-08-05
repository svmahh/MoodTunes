package com.example.moodtunes;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LastfmResponse {
    @SerializedName("tracks")
    private TopTracks tracks;

    public TopTracks getTracks() {
        return tracks;
    }

    public static class TopTracks {
        @SerializedName("track")
        private List<Track> trackList;

        public List<Track> getTrackList() {
            return trackList;
        }
    }

    public static class Track {
        @SerializedName("name")
        private String name;

        @SerializedName("artist")
        private Artist artist;

        public String getName() {
            return name;
        }

        public Artist getArtist() {
            return artist;
        }
    }

    public static class Artist {
        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }
    }
}