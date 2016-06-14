package muzic.coffeemug.com.muzic.Streaming.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import muzic.coffeemug.com.muzic.Activities.BaseActivity;
import muzic.coffeemug.com.muzic.Events.StreamStatusEvent;
import muzic.coffeemug.com.muzic.Events.TrackProgressEvent;
import muzic.coffeemug.com.muzic.Fragments.BaseFragment;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Streaming.Fragments.FragmentRecomsHome;
import muzic.coffeemug.com.muzic.Streaming.Models.SoundCloudTrack;
import muzic.coffeemug.com.muzic.Streaming.Playback.StreamingController;
import muzic.coffeemug.com.muzic.Utilities.MasterPlaybackUtils;

public class RecomsHomeActivity extends BaseActivity {


    private FragmentManager managerFragment;

    // Bottom bar views
    private TextView tvArtistName, tvTitle;
    private ImageView ivAlbumArt, ivPlayPause;
    private int noArtworkDrawable = R.mipmap.ic_launcher;
    private ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoms_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Recommendations");

        // Bottom bar views
        tvArtistName = (TextView) findViewById(R.id.tv_bb_uploader_name);
        tvTitle = (TextView) findViewById(R.id.tv_bb_title);
        ivAlbumArt = (ImageView) findViewById(R.id.iv_bb_album_art);
        ivPlayPause = (ImageView) findViewById(R.id.iv_bb_play_pause);
        pb = (ProgressBar) findViewById(R.id.pb);

        managerFragment = getSupportFragmentManager();
        if (null == managerFragment.findFragmentByTag(FRAG_TAGS.HOME)) {
            FragmentRecomsHome fragmentRecomsHome = FragmentRecomsHome.newInstance();
            managerFragment.beginTransaction().add(R.id.ll_container,
                    fragmentRecomsHome, FRAG_TAGS.HOME).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    public void loadFragment(BaseFragment fragment) {
        managerFragment.beginTransaction().replace(R.id.ll_container,
                fragment, FRAG_TAGS.ONLINE).addToBackStack(null).commit();
    }


    public interface FRAG_TAGS {
        String HOME = "HOME";
        String ONLINE = "ONLINE";
    }


    @Override
    public void onBackPressed() {

        if (managerFragment.getBackStackEntryCount() == 0) {

            if (MasterPlaybackUtils.getInstance().isMasterStreamingServiceRunning(this)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Warning");
                builder.setMessage("Continue back and stop streaming Music?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StreamingController.getInstance(RecomsHomeActivity.this).pauseTrack();
                        RecomsHomeActivity.super.onBackPressed();
                    }
                });
                builder.create().show();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }


    public void setUpBottomBar(SoundCloudTrack track) {

        final String strTrackType = track.track_type;
        final String strTitle = track.title;
        final String strUploader = track.user.username;

        if (!TextUtils.isEmpty(strTitle)) {
            tvTitle.setText(strTitle);
        }

        String strInfo = "";

        if (!TextUtils.isEmpty(strUploader)) {
            strInfo += strUploader;
        }

        if (!TextUtils.isEmpty(strTrackType)) {
            strInfo += "  |  ";
            strInfo += strTrackType;
        }

        tvArtistName.setText(strInfo);
        String strAlbumArtURL = track.artwork_url;
        if (!TextUtils.isEmpty(strAlbumArtURL)) {
            Picasso.with(this).load(strAlbumArtURL).error(noArtworkDrawable).into(ivAlbumArt);
        } else {
            ivAlbumArt.setImageResource(noArtworkDrawable);
        }

        setStreamingIcons();
    }


    @Subscribe
    public void onEvent(StreamStatusEvent event) {
        if (null != event) {
            if (event.isPlaying()) {
                setPlayingIcons();
            }
        }
    }


    private void setPlayingIcons() {
        pb.setVisibility(View.INVISIBLE);
    }


    private void setStreamingIcons() {
        pb.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

}
