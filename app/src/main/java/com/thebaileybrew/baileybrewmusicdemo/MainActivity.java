package com.thebaileybrew.baileybrewmusicdemo;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    ArrayList<song> SongList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recycler = findViewById(R.id.song_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        final TextView currentSongArtist = findViewById(R.id.current_playing_title);
        final TextView currentSongTitle = findViewById(R.id.current_playing_song);

        //TODO: Create SongList Array
        initSongList();

        SongAdapter adapter = new SongAdapter(this, SongList, new CustomClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(MainActivity.this, "Song Selected: ", Toast.LENGTH_SHORT).show();
                String currentArtist = SongList.get(position).getArtistName();
                currentSongArtist.setText(currentArtist);
                String currentSong = SongList.get(position).getArtistSong();
                currentSongTitle.setText(currentSong);
                int currentImage = SongList.get(position).getArtistResource();
                int currentSongLength = SongList.get(position).getArtistSongLength();
                int currentSongRawResource = SongList.get(position).getArtistResource();
            }
        });
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);



    }

    private void initSongList() {
        //SongList.add(new song( String, String, int, int, int);
    }
}
