package com.thebaileybrew.baileybrewmusicdemo;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    String trackTime;
    String currentTime;
    int audioIndex;
    ImageView play;
    ImageView stop;
    ImageView forward;
    ImageView skipForward;
    ImageView rewind;
    ImageView skipBack;
    TextView currentSongTime;
    TextView totalSongTime;
    TextView currentSongArtist;
    TextView currentSongTitle;
    RecyclerView recycler;
    LinearLayoutManager llm;

    private ArrayList<song> SongList = new ArrayList<>();
    private GetSongResource gsr = new GetSongResource();
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initSongList();
        initButtons();

        SongAdapter adapter = new SongAdapter(this, SongList, new CustomClickInterface() {
            @Override
            public void onItemClick(View v, int position) {
                mSeekBar.setProgress(0);
                currentSongTime.setText("00:00");
                //Define selected song (only necessary for listed items)
                song selectedTrack = SongList.get(position);
                audioIndex = selectedTrack.getIdValue();
                String currentArtist = selectedTrack.getArtistName();
                String currentSong = selectedTrack.getArtistSong();
                currentSongArtist.setText(currentArtist);
                currentSongTitle.setText(currentSong);
                Toast.makeText(MainActivity.this, "Song Selected: " + currentArtist + ": " + currentSong, Toast.LENGTH_SHORT).show();
                trackTime = gsr.GetSong(selectedTrack);
                totalSongTime.setText(trackTime);
                int currentSongRawResource = selectedTrack.getArtistResource();
                initCurrentSong(currentSongRawResource);

            }
        });
        recycler.setLayoutManager(llm);
        recycler.setAdapter(adapter);

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer != null) {
                    int currentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                    long duration = mMediaPlayer.getCurrentPosition();
                    mSeekBar.setProgress(currentPosition);
                    String songTime = String.format(Locale.US, "%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds((TimeUnit.MILLISECONDS.toMinutes(duration))));
                    currentSongTime.setText(songTime);
                }
                mHandler.postDelayed(this,1000);
            }
        });




    }
    //Declare your view ids
    private void initViews() {
        recycler = findViewById(R.id.song_list);
        llm = new LinearLayoutManager(this);
        currentSongArtist = findViewById(R.id.current_playing_title);
        currentSongTitle = findViewById(R.id.current_playing_song);
        mSeekBar = findViewById(R.id.current_playing_seekbar);
        mSeekBar.setOnSeekBarChangeListener(this);
        currentSongTime = findViewById(R.id.current_time);
        totalSongTime = findViewById(R.id.total_time);
    }

    //Find and initialize the navigation buttons, also sets the OnClickListeners
    private void initButtons() {
        play = findViewById(R.id.track_play);
        stop = findViewById(R.id.track_stop);
        forward = findViewById(R.id.track_fast_forward);
        rewind = findViewById(R.id.track_rewind);
        skipBack = findViewById(R.id.track_back);
        skipForward = findViewById(R.id.track_skip);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        forward.setOnClickListener(this);
        rewind.setOnClickListener(this);
        skipBack.setOnClickListener(this);
        skipForward.setOnClickListener(this);
    }

    //Playback module for MediaPlayer
    private void initCurrentSong(int song) {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(MainActivity.this, song);
            mSeekBar.setMax(mMediaPlayer.getDuration() / 1000);
        } else {
            mMediaPlayer.release();
            mMediaPlayer = MediaPlayer.create(MainActivity.this,song);
            mSeekBar.setMax(mMediaPlayer.getDuration() / 1000);
        }
    }

    //Creates the ArrayList of all songs
    private void initSongList() {
        //SongList.add(new song( String, String, int, int, int);
        SongList.add(new song("Luca Stricagnoli", "Thunderstruck", R.drawable.luca, R.raw.luca_thunderstruck, 0));
        SongList.add(new song("Luca Stricagnoli", "Bittersweet Symphony", R.drawable.luca, R.raw.luca_bitter_sweet, 1));
        SongList.add(new song("Luca Stricagnoli", "Feel Good Inc", R.drawable.luca, R.raw.luca_cant_stop, 2));
        SongList.add(new song("Luca Stricagnoli", "Sweet Child O' Mine", R.drawable.luca, R.raw.luca_sweet_child, 3));
        SongList.add(new song("Luca Stricagnoli","Can't Stop", R.drawable.luca, R.raw.luca_cant_stop, 4));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        //Sets the functionality of the media buttons
        switch (v.getId()) {
            case R.id.track_play: //Play selected track
                if (mMediaPlayer == null) {
                    Toast.makeText(this, "Please choose a song first...", Toast.LENGTH_SHORT).show();
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        play.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                        mMediaPlayer.pause();
                    } else {
                        play.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                        mMediaPlayer.start();
                    }
                }
                break;
            case R.id.track_stop: //Stop selected track
                if (mMediaPlayer == null) {
                    Toast.makeText(this, "No song currently playing", Toast.LENGTH_SHORT).show();
                } else if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                    mSeekBar.setProgress(0);
                    currentSongTime.setText(" ");
                    totalSongTime.setText("00:00");
                    currentSongArtist.setText("00:00");
                    currentSongTitle.setText(" ");
                }
                break;
            case R.id.track_rewind: //Reverse 10 seconds
                if (mMediaPlayer == null) {
                    Toast.makeText(this, "Can't reverse 10 seconds. No song selected", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mMediaPlayer.getDuration() - mMediaPlayer.getCurrentPosition() < 100000) {
                    mMediaPlayer.seekTo(0);
                } else {
                    mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition() - 10000);
                    currentSongTime.setText(setTrackTime(mMediaPlayer.getCurrentPosition()));
                }
                break;
            case R.id.track_fast_forward: //Forward 10 seconds
                if (mMediaPlayer == null) {
                    Toast.makeText(this, "Can't forward 10 seconds. No song selected", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mMediaPlayer.seekTo((mMediaPlayer.getCurrentPosition()) + 10000);
                }
                break;
        }
    }

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar  The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range min..max where min
     *                 and max were set by {@link //ProgressBar#setMin(int)} and
     *                 {@link //ProgressBar#setMax(int)}, respectively. (The default values for
     *                 min is 0 and max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(mMediaPlayer != null && fromUser) {
            mMediaPlayer.seekTo(progress * 1000);
        }
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     *
     * @param seekBar The SeekBar in which the touch gesture began
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * Called when the end of a media source is reached during playback.
     *
     * @param mp the MediaPlayer that reached the end of the file
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mMediaPlayer.release();
        mSeekBar.setProgress(0);
        currentSongTime.setText("00:00");
        totalSongTime.setText("00:00");
    }

    //Method to set the elapsed time
    public String setTrackTime(int time) {
        currentTime = String.format(Locale.US, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds((TimeUnit.MILLISECONDS.toMinutes(time))));
        return currentTime;
    }
}
