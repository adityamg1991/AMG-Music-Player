package muzic.coffeemug.com.muzic.Adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;

/**
 * Created by aditya on 02/09/15.
 */
public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    ArrayList<Track> dataSet;


    public TrackListAdapter(ArrayList<Track> dataSet) {
        this.dataSet = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Track track = dataSet.get(position);

        String strArtist = track.getArtist();
        String strTitle = track.getTitle();
        String strAlbumName = track.getAlbumName();

        if(!TextUtils.isEmpty(strTitle)) {
            holder.mTextView.setText(strTitle);
        }

        if(!TextUtils.isEmpty(strArtist)) {
            holder.tvArtist.setText(strArtist);
        }

        if(!TextUtils.isEmpty(strAlbumName)) {
            holder.tvAlbum.setText(strAlbumName);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public TextView tvArtist;
        public TextView tvAlbum;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
            tvArtist = (TextView) v.findViewById(R.id.tv_artist);
            tvAlbum = (TextView) v.findViewById(R.id.tv_album);
        }
    }

}
