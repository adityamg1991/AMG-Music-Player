package muzic.coffeemug.com.muzic.Streaming.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Dialogs.TrackOptionsDialog;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Streaming.Models.SoundCloudTrack;
import muzic.coffeemug.com.muzic.Streaming.Playback.StreamingController;
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
                .inflate(R.layout.track_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SoundCloudTrack track = dataSet.get(position);

        String strTrackType = track.track_type;
        String strTitle = track.title;
        String strTagList = track.tag_list;

        if (!TextUtils.isEmpty(strTitle)) {
            holder.mTextView.setText(strTitle);
        }

        String strInfo = "";

        if (!TextUtils.isEmpty(strTrackType)) {
            strInfo += strTrackType;
            strInfo += "  |  ";
        }

        if (!TextUtils.isEmpty(strTagList)) {
            strInfo += strTagList;
        }

        holder.tvInfo.setText(strInfo);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StreamingController.getInstance(mContext).playTrack(track.id);
            }
        });

        holder.llContainer.setTag(position);
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public TextView tvInfo;
        public LinearLayout llContainer;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            tvInfo = (TextView) v.findViewById(R.id.tv_info);
            llContainer = (LinearLayout) v.findViewById(R.id.ll_container);
        }
    }

}
