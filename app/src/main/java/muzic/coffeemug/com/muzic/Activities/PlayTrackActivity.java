package muzic.coffeemug.com.muzic.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import muzic.coffeemug.com.muzic.Adapters.PlayTrackPagerAdapter;
import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Fragments.AlbumArtFragment;
import muzic.coffeemug.com.muzic.MusicPlayback.MusicPlaybackController;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Utilities.Constants;

public class PlayTrackActivity extends TrackBaseActivity implements View.OnClickListener{

    private Context mContext;
    private Track currentTrack;
    private PlayTrackPagerAdapter adapter;
    private ImageView ivPlayPause;
    private MusicPlaybackController controller;

    private TextView tvTotalTime, tvCurrentTime, tvTrackName, tvAdditionalInfo;
    private SeekBar seekBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        mContext = this;
        controller = MusicPlaybackController.getInstance(this);

        ViewPager mPager = (ViewPager) findViewById(R.id.vp_player);
        adapter = new PlayTrackPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);

        mPager.addOnPageChangeListener(new MyPagerChangeListener());
        findViewById(R.id.iv_drop_down).setOnClickListener(this);

        seekBar = (SeekBar) findViewById(R.id.seekBarDistance);
        tvTrackName = (TextView) findViewById(R.id.tv_track_title);
        tvAdditionalInfo = (TextView) findViewById(R.id.tv_add_info);

        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        ivPlayPause = (ImageView) findViewById(R.id.iv_play_pause);
        ivPlayPause.setOnClickListener(this);

        retrieveTrackFromMemoryAndSetUpTrackInfo();
    }


    @Override
    protected void onResume() {
        super.onResume();
        playPauseButtonDecider();
        LocalBroadcastManager.getInstance(this).
                registerReceiver(broadcastReceiver, new IntentFilter(Constants.TRACK_UPDATE_FROM_SERVICE));
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }


    /**
     * Receives updates from Music Playback Service about Track progress
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(null != intent) {

                if(intent.hasExtra(Constants.TRACK_PROGRESSION_UPDATE_KEY)) {
                    int trackProgression = intent.getIntExtra(Constants.TRACK_PROGRESSION_UPDATE_KEY, -1);
                    if(-1 != trackProgression) {
                        seekBar.setProgress(trackProgression);
                        String strCurrentTime = getTimeString(trackProgression / 1000);
                        tvCurrentTime.setText(strCurrentTime);
                    }
                } else if(intent.hasExtra(Constants.UPDATE_TRACK)){
                    // Use case : User opens app for the first time. Directly clicks on Play Button.
                    retrieveTrackFromMemoryAndSetUpTrackInfo();
                }
            }
        }
    };


    /**
     * Decides which button should be visible, Play or Pause.
     */
    private void playPauseButtonDecider() {

        ivPlayPause.setImageResource(android.R.color.transparent);
        if(controller.getIsPlaying(mContext)) {
            ivPlayPause.setImageResource(R.drawable.selector_pause);
        } else {
            ivPlayPause.setImageResource(R.drawable.selector_play);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_drop_down : {
                onBackPressed();
                break;
            }

            case R.id.iv_play_pause : {
                handlePlayPause();
                break;
            }
        }
    }


    private void handlePlayPause() {

        ivPlayPause.setImageResource(android.R.color.transparent);

        if(controller.getIsPlaying(mContext)) {
            // It was playing, pausing it
            ivPlayPause.setImageResource(R.drawable.selector_play);
            controller.pauseTrack();
        } else {
            // It was paused, resuming
            ivPlayPause.setImageResource(R.drawable.selector_pause);
            controller.resumeTrack();
        }
    }


    private void retrieveTrackFromMemoryAndSetUpTrackInfo() {

        currentTrack = SharedPrefs.getInstance(this).getStoredTrack();
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

            try {
                tvCurrentTime.setText("00:00");
                seekBar.setMax((int) currentTrack.getDuration());
                long durationInSec = currentTrack.getDuration() / 1000;
                String strTrackDuration = getTimeString(durationInSec);
                tvTotalTime.setText(strTrackDuration);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void throwSearchedTrackBack(Track track) {
        saveInPrefsAndPlayTrack(track);
        retrieveTrackFromMemoryAndSetUpTrackInfo();
        ivPlayPause.setImageResource(R.drawable.selector_pause);

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


    private String getTimeString(long durationInSec) {

        String str = "";

        try {
            String strMins = String.valueOf(durationInSec / 60);
            if(strMins.length() == 1) {
                strMins = "0" + strMins;
            }

            String strSecs = String.valueOf(durationInSec % 60);
            if(strSecs.length() == 1) {
                strSecs = "0" + strSecs;
            }

            str = strMins + ":" + strSecs;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return str;
    }
}
