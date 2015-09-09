package muzic.coffeemug.com.muzic.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import muzic.coffeemug.com.muzic.BaseActivity;
import muzic.coffeemug.com.muzic.R;

public class SearchActivity extends BaseActivity {

    private final String LABEL_STRING = "Search";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initActionBar();
        enableBack();
        setTitle("");
        makeBackIconWhite();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.voice_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
