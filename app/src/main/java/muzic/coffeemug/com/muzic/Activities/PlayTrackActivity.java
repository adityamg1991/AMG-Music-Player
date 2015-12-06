package muzic.coffeemug.com.muzic.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import muzic.coffeemug.com.muzic.Adapters.PlayTrackPagerAdapter;
import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Fragments.AlbumArtFragment;
import muzic.coffeemug.com.muzic.R;

public class PlayTrackActivity extends TrackBaseActivity implements View.OnClickListener{

    private Track currentTrack;
    private PlayTrackPagerAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);

        ViewPager mPager = (ViewPager) findViewById(R.id.vp_player);
        adapter = new PlayTrackPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);

        mPager.addOnPageChangeListener(new MyPagerChangeListener());
        retrieveTrackFromMemoryAndSetUpTrackInfo();
        findViewById(R.id.iv_drop_down).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_drop_down : {
                onBackPressed();
                break;
            }
        }
    }


    private void retrieveTrackFromMemoryAndSetUpTrackInfo() {

        currentTrack = SharedPrefs.getInstance(this).getStoredTrack();

        TextView tvTrackName = (TextView) findViewById(R.id.tv_track_title);
        TextView tvAdditionalInfo = (TextView) findViewById(R.id.tv_add_info);
        tvTrackName.setSelected(true);

        if(null != currentTrack) {

            String strTrackName = currentTrack.getTitle();
            String strInfo = currentTrack.getArtist();

            if(!TextUtils.isEmpty(strTrackName)) {
                tvTrackName.setText(strTrackName);
            }

            if(!TextUtils.isEmpty(strInfo)) {
                tvAdditionalInfo.setText(strInfo);
            }
        }
    }

    @Override
    public void throwSearchedTrackBack(Track track) {
        SharedPrefs.getInstance(this).storeTrack(track);
        retrieveTrackFromMemoryAndSetUpTrackInfo();

        ((AlbumArtFragment) adapter.getItem(0)).setAlbumArt();
    }


    private class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        private final ImageView ivOne = (ImageView) findViewById(R.id.iv_one);
        private final ImageView ivTwo = (ImageView) findViewById(R.id.iv_two);

        private final int selected = R.drawable.white_circle;
        private final int selectedNot = R.drawable.gray_circle;

        public void onPageSelected(int position) {

            if(position == PlayTrackPagerAdapter.POS_ALBUM_ART) {
                setSelected(ivOne);
                setSelectedNot(ivTwo);
            } else if(position == PlayTrackPagerAdapter.POS_TRACK_LIST) {
                setSelected(ivTwo);
                setSelectedNot(ivOne);
            }
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        public void onPageScrollStateChanged(int state) { }


        private void setSelected(ImageView view) {
            view.setImageResource(selected);
        }

        private void setSelectedNot(ImageView view) {
            view.setImageResource(selectedNot);
        }
    }


    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_out_to_bottom);
    }
}
