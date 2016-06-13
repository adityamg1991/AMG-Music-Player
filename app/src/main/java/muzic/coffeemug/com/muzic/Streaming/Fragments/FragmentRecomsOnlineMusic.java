package muzic.coffeemug.com.muzic.Streaming.Fragments;


import android.os.Bundle;
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
import muzic.coffeemug.com.muzic.Streaming.Adapters.SoundCloudTrackListAdapter;
import muzic.coffeemug.com.muzic.Streaming.Models.SoundCloudTrack;
import muzic.coffeemug.com.muzic.Streaming.Store.StreamTrackStore;

public class FragmentRecomsOnlineMusic extends BaseFragment {

    private RecyclerView recyclerView;

    public static FragmentRecomsOnlineMusic newInstance() {
        Bundle args = new Bundle();
        FragmentRecomsOnlineMusic fragment = new FragmentRecomsOnlineMusic();
        fragment.setArguments(args);
        return fragment;
    }


    public FragmentRecomsOnlineMusic() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recoms_step_two, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recoms_step_two);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<SoundCloudTrack> dataSet = StreamTrackStore.getInstance().getDataSet();
        if (null != dataSet && !dataSet.isEmpty()) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new SoundCloudTrackListAdapter(getActivity(), dataSet));
        }

    }
}
