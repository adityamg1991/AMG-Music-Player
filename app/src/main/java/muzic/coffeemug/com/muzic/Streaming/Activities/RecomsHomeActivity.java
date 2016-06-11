package muzic.coffeemug.com.muzic.Streaming.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import muzic.coffeemug.com.muzic.Activities.BaseActivity;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Streaming.Fragments.FragmentRecomsHome;

public class RecomsHomeActivity extends BaseActivity {


    private FragmentManager managerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoms_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Recoms");

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


    public interface FRAG_TAGS {
        String HOME = "HOME";
    }
}
