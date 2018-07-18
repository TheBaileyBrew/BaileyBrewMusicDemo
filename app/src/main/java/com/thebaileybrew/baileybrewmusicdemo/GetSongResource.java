package com.thebaileybrew.baileybrewmusicdemo;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GetSongResource {
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    public String GetSong(song currentSong) {

        String currentResource = String.valueOf(currentSong.getArtistResource());
        //Calculates the length of the audio track
        Uri uri = Uri.parse("android.resource://" + App.getContext().getPackageName() + "/raw/" + currentResource);
        mmr.setDataSource(App.getContext(), uri);
        long duration = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        return String.format(Locale.US, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds((TimeUnit.MILLISECONDS.toMinutes(duration))));
    }
}
