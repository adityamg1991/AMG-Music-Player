package muzic.coffeemug.com.muzic.Streaming.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Adapters.TrackListAdapter;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Database.DatabaseHelper;
import muzic.coffeemug.com.muzic.Fragments.AlbumArtFragment;
import muzic.coffeemug.com.muzic.Fragments.BaseFragment;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Streaming.Activities.RecomsHomeActivity;
import muzic.coffeemug.com.muzic.Streaming.Models.SoundCloudTrack;
import muzic.coffeemug.com.muzic.Streaming.Store.StreamTrackStore;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Utilities.App;

public class FragmentRecomsHome extends BaseFragment implements AppConstants {


    private static final String LOG_TAG = "FragmentRecomsHome";
    private RecyclerView rvRecon;
    private FrameLayout frameLayout;
    private TrackResultReceiver mTrackResultReceiver;
    private RecomsHomeActivity activity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (RecomsHomeActivity) activity;
    }


    public FragmentRecomsHome() {
        mTrackResultReceiver = new TrackResultReceiver(new Handler());
    }


    public static FragmentRecomsHome newInstance() {
        Bundle args = new Bundle();
        FragmentRecomsHome fragment = new FragmentRecomsHome();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_recom_home, container, false);
        rvRecon = (RecyclerView) view.findViewById(R.id.rv_recoms_step_one);
        frameLayout = (FrameLayout) view.findViewById(R.id.container);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<Track> dataSet = DatabaseHelper.getInstance(getActivity()).getTracksStoredInDatabase();

        if (dataSet.isEmpty()) {
            rvRecon.setVisibility(View.GONE);
            final String strMessage = getString(R.string.no_recoms);
            final Snackbar snackbar = Snackbar.make(frameLayout, strMessage, Snackbar.LENGTH_LONG);
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    getActivity().onBackPressed();
                }
            });
            snackbar.show();
        } else {
            rvRecon.setHasFixedSize(true);
            rvRecon.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvRecon.setAdapter(new TrackListAdapter(getActivity(), dataSet, mTrackResultReceiver, false));
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

            if (Activity.RESULT_OK == resultCode) {
                if (null != resultData) {
                    if (resultData.containsKey(AppConstants.SELECTED_TRACK)) {
                        Track selectedTrack = resultData.getParcelable(AppConstants.SELECTED_TRACK);
                        if (null != selectedTrack) {
                            Log.d(LOG_TAG, "Track : " + selectedTrack.toString());
                            hitApiRequest(selectedTrack);
                        }
                    }
                }
            }
        }
    }


    private void hitApiRequest(Track selectedTrack) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Please wait ... ");
        pd.show();

        final String strTrackName = selectedTrack.getTitle();
        final String strArtistName = selectedTrack.getArtist();
        final String strQuery = strTrackName + "+" + strArtistName;
        final String url = SoundCloud.SEARCH_URL + "&q=" + App.getInstance().urlEncode(strQuery);
        Log.d(LOG_TAG, "URL Similar Tracks : " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                Log.d(LOG_TAG, "Response : " + response);
                handleResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.d(LOG_TAG, "Error : " + error.toString());
                App.getInstance().showToast(error.toString(), getActivity());
            }
        });

        App.getInstance().getRequestQueue().add(stringRequest);

    }


    private void handleResponse(String response) {

        try {
            SoundCloudTrack dataSet[] = new Gson().
                    fromJson(response, SoundCloudTrack[].class);
            if (dataSet.length == 0) {
                App.getInstance().showToast(getString(R.string.no_sug_found), getActivity());
            } else {
                StreamTrackStore.getInstance().setDataSet(dataSet);
                FragmentRecomsOnlineMusic fragment = FragmentRecomsOnlineMusic.newInstance();
                activity.loadFragment(fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            App.getInstance().showToast(getString(R.string.error), getActivity());
        }

    }


}
