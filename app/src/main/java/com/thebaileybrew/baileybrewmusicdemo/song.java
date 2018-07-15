package com.thebaileybrew.baileybrewmusicdemo;

public class song {

    private final String artistName;
    private final String artistSong;
    private final int artistBackground;
    private final int artistResource;
    private final int artistSongLength;

    public song(String artistName, String artistSong, int artistBackground, int artistResource, int artistSongLength) {
        this.artistName = artistName;
        this.artistSong = artistSong;
        this.artistBackground = artistBackground;
        this.artistResource = artistResource;
        this.artistSongLength = artistSongLength;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistSong() {
        return artistSong;
    }

    public int getArtistBackground() {
        return artistBackground;
    }

    public int getArtistResource() {
        return artistResource;
    }

    public int getArtistSongLength() {
        return artistSongLength;
    }
}
