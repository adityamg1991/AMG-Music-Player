package muzic.coffeemug.com.muzic.Activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Adapters.TrackListAdapter;
import muzic.coffeemug.com.muzic.Data.ScrollData;
import muzic.coffeemug.com.muzic.Events.PlaybackStatusEvent;
import muzic.coffeemug.com.muzic.MusicPlayback.MasterPlaybackController;
import muzic.coffeemug.com.muzic.MusicPlayback.MasterPlaybackUtils;
import muzic.coffeemug.com.muzic.Streaming.Activities.RecomsHomeActivity;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Utilities.App;


public class HomeActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ScrollListener scrollListener;

    private float pixelsToMove;
    private TrackResultReceiver mTrackResultReceiver;
    private Context mContext;


    private View bottomBar;
    private TextView tvTrackName;
    private TextView tvArtistName;
    private ImageView ivAlbumArt;

    private TrackListAdapter mTrackListAdapter;
    private ArrayList<Track> mTrackList;

    private final int SEARCH_REQUEST_CODE = 1;
    private Bitmap bmpNoAlbumArt;

    private SharedPrefs prefs;
    private ImageView ivPlayPause;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = this;

        prefs = SharedPrefs.getInstance(this);
        bmpNoAlbumArt = BitmapFactory.decodeResource(getResources(), R.drawable.no_album_art_black_small);
        mTrackResultReceiver = new TrackResultReceiver(new Handler());
        pixelsToMove = App.pxFromDp(mContext, 60);

        scrollListener = new ScrollListener();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(scrollListener);

        initActionBar();
        setTitle(prefs.getHomeLabel());

        bottomBar = findViewById(R.id.bottom_bar);
        ivPlayPause = (ImageView) bottomBar.findViewById(R.id.iv_play_pause);
        tvTrackName = (TextView) bottomBar.findViewById(R.id.tv_track_name);
        tvArtistName = (TextView) bottomBar.findViewById(R.id.tv_artist_name);
        ivAlbumArt = (ImageView) bottomBar.findViewById(R.id.iv_album_art);
        bottomBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startPlayTrackActivity();
            }
        });


        ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePlayPause();
            }
        });

        TrackStore.getInstance(this).readyTracks(this, mTrackResultReceiver);
    }


    private void handlePlayPause() {

        ivPlayPause.setImageResource(android.R.color.transparent);

        if(MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(this)) {
            MasterPlaybackController.getInstance(this).pauseTrack();
        } else {
            MasterPlaybackController.getInstance(this).resumeTrack();
        }
    }



    private void startPlayTrackActivity() {

        startActivity(new Intent(HomeActivity.this, PlayTrackActivity.class));
        overridePendingTransition(R.anim.pull_up_from_bottom, android.R.anim.fade_out);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initialiseBottomBar();
        EventBus.getDefault().register(this);
        playPauseButtonDecider();
    }


    private void playPauseButtonDecider() {

        ivPlayPause.setImageResource(android.R.color.transparent);
        if(MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(this)) {
            setPauseIcon();
        } else {
            setPlayIcon();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    protected void onDestroy() {
        super.onDestroy();
        ScrollData.getInstance().reset();
    }


    private void updateUI() {

        initialiseBottomBar();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        mTrackListAdapter = new TrackListAdapter(mContext, mTrackList, mTrackResultReceiver, true);
        recyclerView.setAdapter(mTrackListAdapter);
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.rate_app: {
                openAppOnMarketPlace();
                break;
            }
            case R.id.scroll_to_top: {
                scrollToTop(recyclerView);
                break;
            }
            case R.id.search: {
                startActivityForResult(new Intent(mContext, SearchActivity.class), SEARCH_REQUEST_CODE);
                break;
            }
            case R.id.voice_play: {
                voicePlay();
                break;
            }
            case R.id.change_label: {
                showChangeLabelDialog();
                break;
            }
            case R.id.share_app: {
                shareApp();
                break;
            }
            case R.id.get_recoms: {
                getRecoms();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void getRecoms() {
        startActivity(new Intent(this, RecomsHomeActivity.class));
    }


    private void shareApp() {

    }


    private void showChangeLabelDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_home_label_edit);
        dialog.show();

        dialog.getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT
                        , WindowManager.LayoutParams.WRAP_CONTENT);

        final EditText etHomeLabel = (EditText) dialog.findViewById(R.id.et_home_label);
        dialog.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strLabel = etHomeLabel.getText().toString();
                if (TextUtils.isEmpty(strLabel)) {
                    showToast(getString(R.string.err_home_label_empty));
                } else {
                    prefs.setHomeLabel(strLabel);
                    setTitle(strLabel);
                    dialog.dismiss();
                }
            }
        });

    }


    private void voicePlay() {
        openSearchDialog(getString(R.string.voice_search_text));
    }


    private class ScrollListener extends RecyclerView.OnScrollListener {

        private boolean isAnimationOngoing = false;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // Have to do because of a bug in Google's Support Library
            setTitle(prefs.getHomeLabel());

            if(isAnimationOngoing) {
                return;
            }

            int scrollDistanceBefore = ScrollData.getInstance().getScrolledDistance();
            boolean isBottomBarVisibleBefore = ScrollData.getInstance().isBottomBarVisible();

            if(scrollDistanceBefore > AppConstants.SCROLL_THRESHOLD && isBottomBarVisibleBefore) {
                hideBottomBar();
            } else if(scrollDistanceBefore < -AppConstants.SCROLL_THRESHOLD && !isBottomBarVisibleBefore) {
                showBottomBar();
            }

            boolean isBottomBarVisibleAfter = ScrollData.getInstance().isBottomBarVisible();

            if((isBottomBarVisibleAfter && dy>0) || (!isBottomBarVisibleAfter && dy<0)) {
                int distance = ScrollData.getInstance().getScrolledDistance();
                distance += dy;
                ScrollData.getInstance().setData(isBottomBarVisibleAfter, distance);
            }
        }

        private void showBottomBar() {
            ScrollData.getInstance().setData(true, 0);
            bottomBar.animate().setDuration(300).translationYBy(-pixelsToMove).setListener(new Animator.AnimatorListener() {

                public void onAnimationStart(Animator animation) {
                    isAnimationOngoing = true;
                }

                public void onAnimationEnd(Animator animation) {
                    isAnimationOngoing = false;
                }

                public void onAnimationCancel(Animator animation) {}
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        }

        private void hideBottomBar() {
            ScrollData.getInstance().setData(false, 0);
            bottomBar.animate().setDuration(300).translationYBy(pixelsToMove).setListener(new Animator.AnimatorListener() {

                public void onAnimationStart(Animator animation) {
                    isAnimationOngoing = true;
                }

                public void onAnimationEnd(Animator animation) {
                    isAnimationOngoing = false;
                }

                public void onAnimationCancel(Animator animation) {}
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        }
    }


    @SuppressLint("ParcelCreator")
    private class TrackResultReceiver extends ResultReceiver {

        public TrackResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(Activity.RESULT_OK == resultCode) {

                if(null != resultData) {

                    if(resultData.containsKey(AppConstants.SELECTED_TRACK)) {

                        Track selectedTrack = resultData.getParcelable(AppConstants.SELECTED_TRACK);
                        if(null != selectedTrack) {
                            saveAndShowTrack(selectedTrack);
                        }
                    } else if(resultData.containsKey(AppConstants.DELETED_TRACK)) {

                        Track deletedTrack = resultData.getParcelable(AppConstants.DELETED_TRACK);
                        if(null != deletedTrack) {
                            mTrackList.remove(deletedTrack);
                            mTrackListAdapter.notifyDataSetChanged();
                        }
                    } else if(resultData.containsKey(AppConstants.TRACK_LIST)) {

                        mTrackList = resultData.getParcelableArrayList(AppConstants.TRACK_LIST);
                        if(null != mTrackList) {
                            updateUI();
                        }
                    }
                }
            }

        }

    }


    private void saveAndShowTrack(Track track) {

        TrackStore.getInstance(this).saveInPrefsAndPlayTrack(track);
        initialiseBottomBar();
    }


    private void initialiseBottomBar() {

        Track track = SharedPrefs.getInstance(mContext).getStoredTrack();

        if(null != track) {

            String trackName = track.getTitle();
            String artistName = track.getArtist();

            if(!TextUtils.isEmpty(trackName)) {
                tvTrackName.setText(trackName);
            }

            if(!TextUtils.isEmpty(artistName)) {
                tvArtistName.setText(artistName);
            }

            try {
                Bitmap bmp = App.getInstance().getAlbumArt(track.getAlbumID(), this);
                if(null != bmp) {
                    ivAlbumArt.setImageBitmap(bmp);
                } else {
                    ivAlbumArt.setImageBitmap(bmpNoAlbumArt);
                }
            } catch (Exception e) {
                ivAlbumArt.setImageBitmap(bmpNoAlbumArt);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SEARCH_REQUEST_CODE
                && resultCode == RESULT_OK && null != data) {

            if(data.hasExtra(AppConstants.SELECTED_TRACK)) {

                Track track = data.getParcelableExtra(AppConstants.SELECTED_TRACK);
                if(null != track) {
                    saveAndShowTrack(track);
                }
            }
        } else if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String match = matches.get(0);
            Track track = TrackStore.getInstance(this).getTrackByHint(match);
            if(null == track) {
                Toast.makeText(this, "No Track found", Toast.LENGTH_SHORT).show();
            } else {
                saveAndShowTrack(track);
            }
        }
    }


    @Subscribe
    public void onEvent(PlaybackStatusEvent event) {
        if (null != event) {
            boolean playing = event.isPlaying();
            if (playing) {
                setPauseIcon();
                initialiseBottomBar();
            } else {
                setPlayIcon();
            }
        }
    }


    private void setPlayIcon() {
        ivPlayPause.setImageResource(R.drawable.play_solid_white);
    }


    private void setPauseIcon() {
        ivPlayPause.setImageResource(R.drawable.pause_solid_white);
    }

}
