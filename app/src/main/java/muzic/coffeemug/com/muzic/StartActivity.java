package muzic.coffeemug.com.muzic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Adapters.TrackListAdapter;
import muzic.coffeemug.com.muzic.Data.Track;


public class StartActivity extends BaseActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        prepareListOfTracks();
        initActionBar("Muzic");
    }


    private void prepareListOfTracks() {

        new AsyncTask<Void, Void, ArrayList<Track>>() {

            @Override
            protected ArrayList<Track> doInBackground(Void... params) {
                return MuzicApplication.getInstance().getMusicFiles(StartActivity.this);
            }

            @Override
            protected void onPostExecute(ArrayList<Track> list) {
                super.onPostExecute(list);
                updateUI(list);

            }
        }.execute();

    }


    private void updateUI(ArrayList<Track> list) {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(StartActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);

        TrackListAdapter adapter = new TrackListAdapter(list);
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
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
