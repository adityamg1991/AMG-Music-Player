package muzic.coffeemug.com.muzic.Activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Adapters.TrackListAdapter;
import muzic.coffeemug.com.muzic.Data.ScrollData;
import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.Constants;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;


public class TrackListActivity extends BaseActivity {

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = this;

        bmpNoAlbumArt = BitmapFactory.decodeResource(getResources(), R.drawable.no_album_art_black_small);
        mTrackResultReceiver = new TrackResultReceiver(new Handler());
        pixelsToMove = MuzicApplication.pxFromDp(mContext, 60);

        scrollListener = new ScrollListener();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(scrollListener);

        initActionBar();
        setTitle(getString(R.string.track_list_activity_label));

        bottomBar = findViewById(R.id.bottom_bar);
        tvTrackName = (TextView) bottomBar.findViewById(R.id.tv_track_name);
        tvArtistName = (TextView) bottomBar.findViewById(R.id.tv_artist_name);
        ivAlbumArt = (ImageView) bottomBar.findViewById(R.id.iv_album_art);
        bottomBar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startPlayTrackActivity();
            }
        });

        TrackStore.getInstance().readyTracks(this, mTrackResultReceiver);
    }


    private void startPlayTrackActivity() {

        startActivity(new Intent(TrackListActivity.this, PlayTrackActivity.class));
        overridePendingTransition(R.anim.pull_up_from_bottom, android.R.anim.fade_out);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initialiseBottomBar();
    }


    protected void onDestroy() {
        super.onDestroy();
        ScrollData.getInstance().reset();
    }


    private void updateUI() {

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
        }
        return super.onOptionsItemSelected(item);
    }


    private void voicePlay() {
        openSearchDialog(getString(R.string.voice_play));
    }


    private class ScrollListener extends RecyclerView.OnScrollListener {

        private boolean isAnimationOngoing = false;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // Have to do because of a bug in Google's Support Library
            setTitle(getString(R.string.track_list_activity_label));

            if(isAnimationOngoing) {
                return;
            }

            int scrollDistanceBefore = ScrollData.getInstance().getScrolledDistance();
            boolean isBottomBarVisibleBefore = ScrollData.getInstance().isBottomBarVisible();

            if(scrollDistanceBefore > Constants.SCROLL_THRESHOLD && isBottomBarVisibleBefore) {
                hideBottomBar();
            } else if(scrollDistanceBefore < -Constants.SCROLL_THRESHOLD && !isBottomBarVisibleBefore) {
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

                    if(resultData.containsKey(Constants.SELECTED_TRACK)) {

                        Track selectedTrack = resultData.getParcelable(Constants.SELECTED_TRACK);
                        if(null != selectedTrack) {
                            saveAndShowTrack(selectedTrack);
                        }
                    } else if(resultData.containsKey(Constants.DELETED_TRACK)) {

                        Track deletedTrack = resultData.getParcelable(Constants.DELETED_TRACK);
                        if(null != deletedTrack) {
                            mTrackList.remove(deletedTrack);
                            mTrackListAdapter.notifyDataSetChanged();
                        }
                    } else if(resultData.containsKey(Constants.TRACK_LIST)) {

                        mTrackList = resultData.getParcelableArrayList(Constants.TRACK_LIST);
                        if(null != mTrackList) {
                            updateUI();
                        }
                    }
                }
            }

        }

    }


    private void saveAndShowTrack(Track track) {

        saveTrackInSharedPrefs(track);
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
                Bitmap bmp = MuzicApplication.getInstance().getAlbumArt(track.getAlbumID(), this);
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

            if(data.hasExtra(Constants.SELECTED_TRACK)) {

                Track track = data.getParcelableExtra(Constants.SELECTED_TRACK);
                if(null != track) {
                    saveAndShowTrack(track);
                }
            }
        } else if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String match = matches.get(0);
            Track track = TrackStore.getInstance().getTrackByHint(match);
            if(null == track) {
                Toast.makeText(this, "No Track found", Toast.LENGTH_SHORT).show();
            } else {
                saveAndShowTrack(track);
            }
        }
    }
}
