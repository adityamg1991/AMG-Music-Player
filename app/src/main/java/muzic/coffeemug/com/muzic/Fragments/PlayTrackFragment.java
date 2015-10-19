package muzic.coffeemug.com.muzic.Fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import muzic.coffeemug.com.muzic.Adapters.PlayTrackPagerAdapter;
import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;


public class PlayTrackFragment extends BaseFragment implements View.OnClickListener{

    private Track currentTrack;
    private PlayTrackPagerAdapter adapter;

    public static PlayTrackFragment getInstance() {

        PlayTrackFragment instance = new PlayTrackFragment();
        return instance;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_drop_down : {
                getActivity().onBackPressed();
                break;
            }
        }
    }

    private class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        private final ImageView ivOne = (ImageView) getActivity().findViewById(R.id.iv_one);
        private final ImageView ivTwo = (ImageView) getActivity().findViewById(R.id.iv_two);

        private final int selected = R.drawable.white_circle;
        private final int selectedNot = R.drawable.gray_circle;

        public void onPageSelected(int position) {

            if(position == PlayTrackPagerAdapter.POS_ALBUM_ART) {
                setSelected(ivOne);
                setSelectedNot(ivTwo);
            } else if(position == PlayTrackPagerAdapter.POS_TRACK_LIST) {
                setSelected(ivTwo);
                setSelectedNot(ivOne);
            }
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        public void onPageScrollStateChanged(int state) { }


        private void setSelected(ImageView view) {
            view.setImageResource(selected);
        }

        private void setSelectedNot(ImageView view) {
            view.setImageResource(selectedNot);
        }
    }

    public PlayTrackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_track, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewPager mPager = (ViewPager) getActivity().findViewById(R.id.vp_player);
        adapter = new PlayTrackPagerAdapter(getFragmentManager());
        mPager.setAdapter(adapter);

        mPager.addOnPageChangeListener(new MyPagerChangeListener());
        setUpTrackInfo();
        getActivity().findViewById(R.id.iv_drop_down).setOnClickListener(this);
    }


    private void setUpTrackInfo() {

        currentTrack = SharedPrefs.getInstance(getActivity()).getStoredTrack();

        TextView tvTrackName = (TextView) getActivity().findViewById(R.id.tv_track_title);
        TextView tvAdditionalInfo = (TextView) getActivity().findViewById(R.id.tv_add_info);
        tvTrackName.setSelected(true);

        if(null != currentTrack) {

            String strTrackName = currentTrack.getTitle();
            String strInfo = currentTrack.getArtist();

            if(!TextUtils.isEmpty(strTrackName)) {
                tvTrackName.setText(strTrackName);
            }

            if(!TextUtils.isEmpty(strInfo)) {
                tvAdditionalInfo.setText(strInfo);
            }
        }
    }


    public void setSelectedTrack() {
        setUpTrackInfo();
    }
}
