package muzic.coffeemug.com.muzic.Streaming.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Dialogs.TrackOptionsDialog;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Streaming.Activities.RecomsHomeActivity;
import muzic.coffeemug.com.muzic.Streaming.Models.SoundCloudTrack;
import muzic.coffeemug.com.muzic.Streaming.Playback.StreamingController;
import muzic.coffeemug.com.muzic.Utilities.App;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;

/**
 * Created by PAVILION on 6/13/2016.
 */
public class SoundCloudTrackListAdapter extends RecyclerView.Adapter<SoundCloudTrackListAdapter.ViewHolder> {

    private ArrayList<SoundCloudTrack> dataSet;
    private Context mContext;


    public SoundCloudTrackListAdapter(Context context, ArrayList<SoundCloudTrack> dataSet) {
        this.dataSet = dataSet;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_list_item_soundcloud, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SoundCloudTrack track = dataSet.get(position);

        String strTrackType = track.track_type;
        String strTitle = track.title;
        String strUploader = track.user.username;

        if (!TextUtils.isEmpty(strTitle)) {
            holder.tvTitle.setText(strTitle);
        }

        String strInfo = "";

        if (!TextUtils.isEmpty(strUploader)) {
            strInfo += strUploader;
        }

        if (!TextUtils.isEmpty(strTrackType)) {
            strInfo += "  |  ";
            strInfo += strTrackType;
        }

        holder.tvUploader.setText(strInfo);

        String strLikeCount = track.likes_count;
        if (!TextUtils.isEmpty(strLikeCount)) {
            holder.tvLikeCount.setText(strLikeCount);
        }

        String strDuration = track.duration;
        if (!TextUtils.isEmpty(strDuration) && TextUtils.isDigitsOnly(strDuration)) {
            holder.tvDuration.setText(App.getInstance().getTimeString(Integer.parseInt(strDuration) / 1000));
        }

        String strAlbumArtURL = track.artwork_url;
        if (!TextUtils.isEmpty(strAlbumArtURL)) {
            Picasso.with(mContext).load(strAlbumArtURL).into(holder.ivAlbumArt);
        }

        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StreamingController.getInstance(mContext).playTrack(track.id);
                if (mContext instanceof RecomsHomeActivity) {
                    ((RecomsHomeActivity) mContext).setUpBottomBar(track);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUploader, tvTitle, tvDuration, tvLikeCount;
        public ImageView ivAlbumArt;
        public RelativeLayout rlContainer;

        public ViewHolder(View v) {
            super(v);
            tvUploader = (TextView) v.findViewById(R.id.tv_uploader_name);
            tvTitle = (TextView) v.findViewById(R.id.tv_title);
            tvDuration = (TextView) v.findViewById(R.id.tv_duration);
            tvLikeCount = (TextView) v.findViewById(R.id.tv_like_count);
            ivAlbumArt = (ImageView) v.findViewById(R.id.iv_album_art);
            rlContainer = (RelativeLayout) v.findViewById(R.id.rl_container);
        }
    }

}
