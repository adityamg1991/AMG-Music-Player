package muzic.coffeemug.com.muzic.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import muzic.coffeemug.com.muzic.Fragments.PlayTrackFragment;
import muzic.coffeemug.com.muzic.R;

public class PlayTrackActivity extends AppCompatActivity {

    private final String FRAG_TAG = "frag_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_track);

        FragmentManager managerFragment = getSupportFragmentManager();
        if(null == managerFragment.findFragmentByTag(FRAG_TAG)) {

            PlayTrackFragment playTrackFragment = PlayTrackFragment.getInstance();
            managerFragment.beginTransaction().
                    add(android.R.id.content, playTrackFragment, FRAG_TAG).commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, R.anim.push_out_to_bottom);
    }
}
