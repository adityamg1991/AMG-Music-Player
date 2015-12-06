package muzic.coffeemug.com.muzic.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import muzic.coffeemug.com.muzic.Fragments.AlbumArtFragment;
import muzic.coffeemug.com.muzic.Fragments.SearchTrackFragment;

/**
 * Created by Aditya on 9/20/2015.
 */
public class PlayTrackPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_OF_PAGES = 2;

    public static final int POS_ALBUM_ART = 0;
    public static final int POS_TRACK_LIST = 1;

    private AlbumArtFragment albumArtFragment;
    private SearchTrackFragment searchTrackFragment;

    public PlayTrackPagerAdapter(FragmentManager fm) {
        super(fm);

        albumArtFragment = AlbumArtFragment.getInstance();
        searchTrackFragment = SearchTrackFragment.getInstance();
    }

    public Fragment getItem(int position) {

        if(position == POS_ALBUM_ART) {
            return albumArtFragment;
        } else if(position == POS_TRACK_LIST) {
            return searchTrackFragment;
        }

        return null;
    }

    public int getCount() {
        return NUM_OF_PAGES;
    }
}
