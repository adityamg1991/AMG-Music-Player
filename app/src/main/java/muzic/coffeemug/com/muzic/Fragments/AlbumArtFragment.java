package muzic.coffeemug.com.muzic.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;

/**
 * Created by Aditya on 9/20/2015.
 */
public class AlbumArtFragment extends BaseFragment {


    public static AlbumArtFragment getInstance() {
        return new AlbumArtFragment();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_art_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setAlbumArt();
    }


    private void setAlbumArt() {

        ImageView iv = (ImageView) getActivity().findViewById(R.id.iv_album_art);
        Track track = SharedPrefs.getInstance(getActivity()).getStoredTrack();

        if(null != track) {
            try {
                Bitmap bmp = MuzicApplication.getInstance().getHighResAlbumArt(track.getAlbumID(), getActivity());
                if(null != bmp) {
                    iv.setImageBitmap(bmp);
                    return;
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        iv.setImageResource(R.drawable.no_album_art);
    }
}
