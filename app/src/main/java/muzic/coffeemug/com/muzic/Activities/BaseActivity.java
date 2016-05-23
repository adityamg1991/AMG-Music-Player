package muzic.coffeemug.com.muzic.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;

import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.MusicPlayback.MusicPlaybackController;
import muzic.coffeemug.com.muzic.MusicPlaybackV2.MasterPlaybackController;
import muzic.coffeemug.com.muzic.R;

/**
 * Created by aditya on 07/09/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private MusicPlaybackController musicPlaybackController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        musicPlaybackController = MusicPlaybackController.getInstance(this);
    }

    protected void initActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    protected void setTitle(String str) {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + str + "</font>"));
    }


    protected void openAppOnMarketPlace() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException exception) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    protected void scrollToTop(RecyclerView rView) {
        rView.scrollToPosition(0);
    }


    protected void enableBack() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    protected void makeBackIconWhite() {

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }


    protected void saveInPrefsAndPlayTrack(Track track) {
        SharedPrefs.getInstance(this).storeTrack(track);
        //musicPlaybackController.playTrack(this);
        MasterPlaybackController.getInstance(this).saveAndPlayTrack(track);
    }


    protected void openSearchDialog(String msg) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, msg);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }
}
