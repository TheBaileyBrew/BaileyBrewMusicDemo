package com.thebaileybrew.baileybrewmusicdemo;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private ArrayList<song> SongItem;
    private CustomClickInterface clickInterface;
    private LayoutInflater inflater;
    private MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    public SongAdapter(Context context, ArrayList<song> SongItem, CustomClickInterface clickInterface) {
        this.SongItem = SongItem;
        this.clickInterface = clickInterface;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_item, parent, false);
        final SongHolder sHolder = new SongHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInterface.onItemClick(v, sHolder.getLayoutPosition());
            }
        });
        return sHolder;
    }

    @Override
    public void onBindViewHolder(SongHolder holder, int position) {

        song currentSong = SongItem.get(position);
        holder.mArtistName.setText(currentSong.getArtistName());
        holder.mSongName.setText(currentSong.getArtistSong());
        holder.mImageView.setImageResource(currentSong.getArtistBackground());
        String currentResource = String.valueOf(currentSong.getArtistResource());
        GetSongResource gsr = new GetSongResource();
        String songTime = gsr.GetSong(currentSong);
        holder.mSongLength.setText(songTime);
    }

    @Override
    public int getItemCount() {
        return SongItem.size();
    }

    class SongHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mArtistName;
        private TextView mSongName;
        private TextView mSongLength;

        public SongHolder(View songView) {
            super(songView);
            mImageView = songView.findViewById(R.id.artist_image);
            mArtistName = songView.findViewById(R.id.artist_name);
            mSongName = songView.findViewById(R.id.artist_song);
            mSongLength = songView.findViewById(R.id.song_time);
        }
    }
}
