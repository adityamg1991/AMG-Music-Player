package muzic.coffeemug.com.muzic;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Adapters.TrackListAdapter;
import muzic.coffeemug.com.muzic.Data.ScrollData;
import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;


public class TrackListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ScrollListener scrollListener;

    private float pixelsToMove;
    private MyTrackReceiver myTrackReceiver;
    private Context mContext;


    private View bottomBar;
    private TextView tvTrackName;
    private TextView tvArtistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mContext = this;

        myTrackReceiver = new MyTrackReceiver(new Handler());
        pixelsToMove = MuzicApplication.pxFromDp(mContext, 60);

        scrollListener = new ScrollListener();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(scrollListener);

        initActionBar();
        setTitle(getString(R.string.track_list_activity_label));

        bottomBar = findViewById(R.id.bottom_bar);
        tvTrackName = (TextView) bottomBar.findViewById(R.id.tv_track_name);
        tvArtistName = (TextView) bottomBar.findViewById(R.id.tv_artist_name);
        initialiseBottomBar();

        prepareListOfTracks();
    }


    private void prepareListOfTracks() {

        new AsyncTask<Void, Void, ArrayList<Track>>() {

            @Override
            protected ArrayList<Track> doInBackground(Void... params) {
                return MuzicApplication.getInstance().getMusicFiles(mContext);
            }

            @Override
            protected void onPostExecute(ArrayList<Track> list) {
                super.onPostExecute(list);
                updateUI(list);

            }
        }.execute();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScrollData.getInstance().reset();
    }


    private void updateUI(ArrayList<Track> list) {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);

        TrackListAdapter adapter = new TrackListAdapter(mContext, list, myTrackReceiver);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);
        return true;
    }


    @Override
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
        }
        return super.onOptionsItemSelected(item);
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


    private class MyTrackReceiver extends ResultReceiver {

        public MyTrackReceiver(Handler handler) {
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
                            // Store the current track in Shared Preferences
                            SharedPrefs.getInstance(mContext).storeTrack(selectedTrack);
                            // Retrieve current track from Shared Preferences and show display it
                            initialiseBottomBar();
                        }
                    }
                }
            }
        }

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
        }
    }

}
