package muzic.coffeemug.com.muzic.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Fragments.PlayTrackFragment;
import muzic.coffeemug.com.muzic.R;

public class PlayTrackActivity extends TrackBaseActivity {

    private final String FRAG_TAG = "frag_tag";
    private PlayTrackFragment playTrackFragment;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);

        FragmentManager managerFragment = getSupportFragmentManager();
        if(null == managerFragment.findFragmentByTag(FRAG_TAG)) {

            playTrackFragment = PlayTrackFragment.getInstance();
            managerFragment.beginTransaction().
                    add(android.R.id.content, playTrackFragment, FRAG_TAG).commit();
        }
    }


    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_out_to_bottom);
    }


    @Override
    public void throwSearchedTrackBack(Track track) {

        saveTrackInSharedPrefs(track);

        if(null != playTrackFragment) {
            playTrackFragment.setSelectedTrack();
        }
    }
}
