package muzic.coffeemug.com.muzic.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import muzic.coffeemug.com.muzic.Fragments.AlbumArtFragment;
import muzic.coffeemug.com.muzic.Fragments.SearchTrackFragment;

/**
 * Created by Aditya on 9/20/2015.
 */
public class PlayTrackPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_OF_PAGES = 2;

    public static final int POS_ALBUM_ART = 0;
    public static final int POS_TRACK_LIST = 1;

    public PlayTrackPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int position) {

        if(position == POS_ALBUM_ART) {
            return AlbumArtFragment.getInstance();
        } else if(position == POS_TRACK_LIST) {
            return SearchTrackFragment.getInstance();
        }

        return null;
    }

    public int getCount() {
        return NUM_OF_PAGES;
    }
}
