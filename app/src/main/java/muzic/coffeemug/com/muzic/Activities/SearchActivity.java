package muzic.coffeemug.com.muzic.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Fragments.TrackListFragment;
import muzic.coffeemug.com.muzic.R;

public class SearchActivity extends TrackBaseActivity {

    private final String LABEL_STRING = "Search";
    private final String FRAG_TAG = "frag_tag";
    private TrackListFragment mSearchFragment;
    private EditText etSearch;

    private FragmentManager managerFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        managerFragment = getSupportFragmentManager();

        initActionBar();
        enableBack();
        setTitle("");
        makeBackIconWhite();
        attachFragment();

        etSearch = (EditText) findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void afterTextChanged(Editable editable) {}

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(null != mSearchFragment) {
                    mSearchFragment.setQueryString(charSequence.toString());
                }
            }
        });
    }


    private void attachFragment() {

        if(null == managerFragment.findFragmentByTag(FRAG_TAG)) {

            mSearchFragment = TrackListFragment.getInstance();
            managerFragment.beginTransaction().
                    add(R.id.ll_container, mSearchFragment, FRAG_TAG).commit();
        }
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
            openSearchDialog(getString(R.string.voice_search_text));
        }

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            String match = matches.get(0);
            if(!TextUtils.isEmpty(match)) {
                etSearch.setText(match);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void throwSearchedTrackBack(Track track) {

        Intent intent = new Intent();
        intent.putExtra(AppConstants.SELECTED_TRACK, track);
        setResult(RESULT_OK, intent);
        finish();
    }
}
