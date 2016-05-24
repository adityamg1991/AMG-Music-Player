package muzic.coffeemug.com.muzic.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Activities.TrackBaseActivity;
import muzic.coffeemug.com.muzic.Adapters.TrackListAdapter;
import muzic.coffeemug.com.muzic.Utilities.Constants;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Store.TrackStore;


public class TrackListFragment extends BaseFragment {


    private ArrayList<Track> completeTrackList;
    private ArrayList<Track> listSetInAdapter;
    private TrackListAdapter listAdapter;
    private MyResultReceiver resultReceiver;
    private TrackBaseActivity activity;


    public TrackListFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (TrackBaseActivity) activity;
    }

    public static TrackListFragment getInstance() {

        TrackListFragment instance = new TrackListFragment();
        return instance;
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        resultReceiver = new MyResultReceiver(new Handler());
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

        completeTrackList = TrackStore.getInstance().getTrackList();
        listSetInAdapter = new ArrayList<Track>();
        for(Track track : completeTrackList) {
            listSetInAdapter.add(track);
        }

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        listAdapter = new TrackListAdapter(activity, listSetInAdapter, resultReceiver, false);
        recyclerView.setAdapter(listAdapter);
    }


    @SuppressLint("ParcelCreator")
    private class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            if(Activity.RESULT_OK == resultCode) {

                if(null != resultData) {

                    if(resultData.containsKey(Constants.SELECTED_TRACK)) {

                        Track selectedTrack = resultData.getParcelable(Constants.SELECTED_TRACK);
                        if(null != selectedTrack) {
                            activity.throwSearchedTrackBack(selectedTrack);
                        }
                    }
                }
            }
        }

    }


    public void setQueryString(String str) {

        str = str.toLowerCase();
        listSetInAdapter.clear();

        if(TextUtils.isEmpty(str)) {

            for(Track track : completeTrackList) {
                listSetInAdapter.add(track);
            }

        } else {

            for(Track track : completeTrackList) {

                if(track.getDisplayName().toLowerCase().contains(str)
                        || track.getArtist().toLowerCase().contains(str)
                        || track.getAlbumName().toLowerCase().contains(str)
                        || track.getTitle().toLowerCase().contains(str)) {

                    listSetInAdapter.add(track);
                }
            }
        }

        listAdapter.notifyDataSetChanged();
    }
}
