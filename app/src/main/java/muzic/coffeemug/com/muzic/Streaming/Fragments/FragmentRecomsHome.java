package muzic.coffeemug.com.muzic.Streaming.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Adapters.TrackListAdapter;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Database.DatabaseHelper;
import muzic.coffeemug.com.muzic.Fragments.BaseFragment;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;

public class FragmentRecomsHome extends BaseFragment {


    private static final String LOG_TAG = "FragmentRecomsHome";
    private RecyclerView rvRecon;
    private TrackResultReceiver mTrackResultReceiver;


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
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvRecon.setHasFixedSize(true);
        rvRecon.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<Track> dataSet = DatabaseHelper.getInstance(getActivity()).getTracksStoredInDatabase();
        rvRecon.setAdapter(new TrackListAdapter(getActivity(), dataSet, mTrackResultReceiver, false));
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

                        }
                    }
                }
            }
        }
    }


}
