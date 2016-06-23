package muzic.coffeemug.com.muzic.Activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import muzic.coffeemug.com.muzic.Adapters.PlayTrackPagerAdapter;
import muzic.coffeemug.com.muzic.Events.PlaybackStatusEvent;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.App;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Utilities.PlayStyle;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Events.TrackProgressEvent;
import muzic.coffeemug.com.muzic.Fragments.AlbumArtFragment;
import muzic.coffeemug.com.muzic.MusicPlayback.PlaybackController;
import muzic.coffeemug.com.muzic.Utilities.MasterPlaybackUtils;
import muzic.coffeemug.com.muzic.R;

public class PlayTrackActivity extends TrackBaseActivity implements View.OnClickListener {

    private static final String LOG_TAG = "PlayTrackActivity";

    private PlayTrackPagerAdapter adapter;
    private ImageView ivPlayPause, ivForward, ivRewind;

    private TextView tvTotalTime, tvCurrentTime, tvTrackName, tvAdditionalInfo;
    private SeekBar seekBar;

    private PlaybackController masterPlaybackController;

    private final SharedPrefs prefs = SharedPrefs.getInstance(this);
    private final TrackStore trackStore = TrackStore.getInstance(this);
    private RelativeLayout rlContainer;


    @Override
    protected void onResume() {
        super.onResume();
        playPauseButtonDecider();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);
        masterPlaybackController = PlaybackController.getInstance(this);

        ViewPager mPager = (ViewPager) findViewById(R.id.vp_player);
        adapter = new PlayTrackPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);

        mPager.addOnPageChangeListener(new MyPagerChangeListener());
        findViewById(R.id.iv_drop_down).setOnClickListener(this);

        rlContainer = (RelativeLayout) findViewById(R.id.rl_container_play_track);
        seekBar = (SeekBar) findViewById(R.id.seekBarDistance);
        tvTrackName = (TextView) findViewById(R.id.tv_track_title);
        tvAdditionalInfo = (TextView) findViewById(R.id.tv_add_info);

        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        ivPlayPause = (ImageView) findViewById(R.id.iv_play_pause);
        ivPlayPause.setOnClickListener(this);

        ivForward = (ImageView) findViewById(R.id.iv_forward);
        ivRewind = (ImageView) findViewById(R.id.iv_rewind);
        ivForward.setOnClickListener(this);
        ivRewind.setOnClickListener(this);

        retrieveTrackFromMemoryAndSetUpTrackInfo();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(LOG_TAG, "From User : Finger up");
                setTrackProgress(seekBar.getProgress() / 1000);
                prefs.saveTrackProgress(seekBar.getProgress() / 1000);
                masterPlaybackController.moveTrackToPoint();
            }
        });

        showProductTourIfNec();
    }


    public void showProductTourIfNec() {

        if (prefs.getIntroKeyData(SharedPrefs.PRODUCT_TOUR.KEY_PLAY_TRACK_SCREEN)) {

            final AlbumArtFragment albumArtFragment = adapter.getAlbumArtFragment();
            final String strMessage = getString(R.string.product_tour_song_settings);
            final Snackbar snackbar = Snackbar.make(rlContainer, strMessage, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != albumArtFragment) {
                        albumArtFragment.toggleOptionsForProductTour();
                    }
                    snackbar.dismiss();
                    prefs.setIntroKeyDataFalse(SharedPrefs.PRODUCT_TOUR.KEY_PLAY_TRACK_SCREEN);
                }
            });

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(android.R.color.black));
            snackbar.show();

            if (null != albumArtFragment) {
                albumArtFragment.toggleOptionsForProductTour();
            }
        }

    }


    /**
     * Decides which button should be visible, Play or Pause.
     */
    private void playPauseButtonDecider() {

        ivPlayPause.setImageResource(android.R.color.transparent);
        if (MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(this)) {
            setPauseIcon();
        } else {
            setPlayIcon();
        }
    }


    private void setPlayIcon() {
        ivPlayPause.setImageResource(R.drawable.selector_play);
    }


    private void setPauseIcon() {
        ivPlayPause.setImageResource(R.drawable.selector_pause);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_drop_down: {
                onBackPressed();
                break;
            }

            case R.id.iv_play_pause: {
                handlePlayPause();
                break;
            }

            case R.id.iv_forward: {
                masterPlaybackController.playNextTrack();
                break;
            }

            case R.id.iv_rewind: {
                playPreviousTrack();
                break;
            }
        }
    }


    private void playPreviousTrack() {

        int playStyle = prefs.getPlayStyle();
        Track track = null;

        if (/*prefs.getTrackProgress() < 5*/false) {
            // If track has not progressed much, start it all over again
        } else {

            if (PlayStyle.REPEAT_ALL == playStyle) {
                track = trackStore.getPreviousLinearTrack();
            } else if (PlayStyle.REPEAT_ONE == playStyle) {
                // Nothing to do, play the same track again
            } else if (PlayStyle.SHUFFLE == playStyle) {
                track = trackStore.getNextRandomTrack();
            }

        }

        if (null != track) {
            prefs.storeTrack(track);
        }

        PlaybackController.getInstance(this).playTrack();

    }


    private void setUpAlbumArt() {
        adapter.getAlbumArtFragment().setAlbumArt();
    }


    private void handlePlayPause() {

        ivPlayPause.setImageResource(android.R.color.transparent);

        if (MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(this)) {
            pausePlayback();
        } else {
            resumePlayback();
        }
    }


    private void resumePlayback() {
        masterPlaybackController.resumeTrack();
        setPauseIcon();
    }


    private void pausePlayback() {
        masterPlaybackController.pauseTrack();
        setPlayIcon();
    }


    private void retrieveTrackFromMemoryAndSetUpTrackInfo() {

        Track currentTrack = SharedPrefs.getInstance(this).getStoredTrack();
        tvTrackName.setSelected(true);

        if (null != currentTrack) {

            String strTrackName = currentTrack.getTitle();
            String strInfo = currentTrack.getArtist();

            if (!TextUtils.isEmpty(strTrackName)) {
                tvTrackName.setText(strTrackName);
            }

            if (!TextUtils.isEmpty(strInfo)) {
                tvAdditionalInfo.setText(strInfo);
            }

            try {
                tvCurrentTime.setText("00:00");
                seekBar.setMax((int) currentTrack.getDuration());
                long durationInSec = currentTrack.getDuration() / 1000;
                String strTrackDuration = App.getInstance().getTimeString(durationInSec);
                tvTotalTime.setText(strTrackDuration);

                int progress = SharedPrefs.getInstance(this).getTrackProgress();
                if (AppConstants.SharedPref.EMPTY_PROGRESS != progress) {
                    setTrackProgress(progress);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void throwSearchedTrackBack(Track track) {
        TrackStore.getInstance(this).saveInPrefsAndPlayTrack(track);
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

            if (position == PlayTrackPagerAdapter.POS_ALBUM_ART) {
                setSelected(ivOne);
                setSelectedNot(ivTwo);
            } else if (position == PlayTrackPagerAdapter.POS_TRACK_LIST) {
                setSelected(ivTwo);
                setSelectedNot(ivOne);
            }
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageScrollStateChanged(int state) {
        }


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


    @Subscribe
    public void onEvent(TrackProgressEvent event) {
        if (null != event) {
            int progress = event.getProgress();
            setTrackProgress(progress);
        }
    }


    @Subscribe
    public void onEvent(PlaybackStatusEvent event) {
        if (null != event) {
            boolean playing = event.isPlaying();
            if (playing) {
                setPauseIcon();
                setUpAlbumArt();
                retrieveTrackFromMemoryAndSetUpTrackInfo();
            } else {
                setPlayIcon();
            }
        }
    }


    private void setTrackProgress(int progress) {

        tvCurrentTime.setText(App.getInstance().getTimeString(progress));
        seekBar.setProgress(progress * 1000);
    }
}
