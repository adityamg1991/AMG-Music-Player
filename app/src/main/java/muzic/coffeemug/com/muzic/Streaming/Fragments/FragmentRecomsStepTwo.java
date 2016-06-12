package muzic.coffeemug.com.muzic.Streaming.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import muzic.coffeemug.com.muzic.Fragments.BaseFragment;
import muzic.coffeemug.com.muzic.R;

public class FragmentRecomsStepTwo extends BaseFragment {

    private RecyclerView recyclerView;

    public static FragmentRecomsStepTwo newInstance() {
        Bundle args = new Bundle();
        FragmentRecomsStepTwo fragment = new FragmentRecomsStepTwo();
        fragment.setArguments(args);
        return fragment;
    }


    public FragmentRecomsStepTwo() {
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
    }
}
