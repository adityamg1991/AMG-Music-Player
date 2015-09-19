package muzic.coffeemug.com.muzic.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;

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
    }
}
