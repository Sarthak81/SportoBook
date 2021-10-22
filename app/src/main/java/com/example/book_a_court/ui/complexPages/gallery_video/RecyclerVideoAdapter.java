package com.example.book_a_court.ui.complexPages.gallery_video;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.book_a_court.R;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;

import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;



public class RecyclerVideoAdapter extends RecyclerView.Adapter<RecyclerVideoAdapter.ViewHolder> {

    Context context;
    List< videoUploadInfo > VideoUploadInfoList;
    public SimpleExoPlayer exoPlayer;

    public RecyclerVideoAdapter(Context context, List< videoUploadInfo > TempList1) {

        this.VideoUploadInfoList = TempList1;

        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }




    //    @Overrid
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        videoUploadInfo UploadInfo = VideoUploadInfoList.get(position);
   // Toast.makeText(context, ""+UploadInfo.getVideoURL()+UploadInfo.videoURL, Toast.LENGTH_SHORT).show();
        viewHolder.setExoplayer((Application) context.getApplicationContext(), UploadInfo.getVideoURL());

    }


//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
//        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
//
//        holder.imageNameTextView.setText(UploadInfo.getImageName());
//
//        //Loading image from Glide library.
//        //Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);
//        //Toast.makeText(context, ""+UploadInfo.getImageURL(), Toast.LENGTH_SHORT).show();
//        //Picasso.get().load(UploadInfo.getImageURL()).into(holder.imageView);
//        Glide.with(context).asBitmap().fitCenter().load(UploadInfo.getImageURL()).into(holder.imageView);
//    }

    @Override
    public int getItemCount() {

        return VideoUploadInfoList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        SimpleExoPlayer exoPlayer;
        SimpleExoPlayerView exoPlayerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }


        public void setExoplayer(Application application, String videoURL) {
            exoPlayerView = itemView.findViewById(R.id.idExoPlayerVIew);

            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

                // track selector is used to navigate between
                // video using a default seekbar.
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

                // we are adding our track selector to exoplayer.
                exoPlayer = ExoPlayerFactory.newSimpleInstance(application, trackSelector);

                // we are parsing a video url
                // and parsing its video uri.
                Uri videouri = Uri.parse(videoURL);

                // we are creating a variable for datasource factory
                // and setting its user agent as 'exoplayer_view'
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");

                // we are creating a variable for extractor factory
                // and setting it to default extractor factory.
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                // we are creating a media source with above variables
                // and passing our event handler as null,
                MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);

                // inside our exoplayer view
                // we are setting our player
                exoPlayerView.setPlayer(exoPlayer);

                // we are preparing our exoplayer
                // with media source.
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(false);

                //exoPlayer.stop();
                // we are setting our exoplayer
                // when it is ready.
//                if (exoPlayer != null) {
//                    exoPlayer.setPlayWhenReady(true);
//
//                    exoPlayer.seekTo(0);
//                }
            } catch (Exception e) {
                Log.e("viewholder", "exo-error" + e.toString());
            }




        }

    }

}