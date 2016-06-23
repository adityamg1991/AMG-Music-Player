package muzic.coffeemug.com.muzic.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import muzic.coffeemug.com.muzic.Utilities.PlayStyle;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Utilities.App;

/**
 * Created by Aditya on 9/20/2015.
 */
public class AlbumArtFragment extends BaseFragment implements View.OnClickListener{

    private ImageView ivAlbumArt;
    private boolean isControlPanelVisible;
    private RelativeLayout rlSettings;
    private int valueToAnimate = -1;
    private App app;
    private SharedPrefs prefs;
    private ImageView ivPlayStyle;

    public static AlbumArtFragment getInstance() {
        AlbumArtFragment frag = new AlbumArtFragment();
        return frag;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_art_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prefs = SharedPrefs.getInstance(getActivity());
        app = App.getInstance();
        isControlPanelVisible = false;
        ivAlbumArt = (ImageView) getActivity().findViewById(R.id.iv_album_art);
        rlSettings = (RelativeLayout) getActivity().findViewById(R.id.rl_settings);
        setAlbumArt();

        // Click on ImageView and shit
        ivAlbumArt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isControlPanelVisible) {
                    hideSettings();
                } else {
                    showSettings();
                }

            }
        });

        // Get width of Settings View
        ViewTreeObserver vto = rlSettings.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                valueToAnimate = rlSettings.getWidth();
            }
        });

        // Set up clicks
        getActivity().findViewById(R.id.iv_sound_settings).setOnClickListener(this);
        getActivity().findViewById(R.id.iv_share).setOnClickListener(this);

        ivPlayStyle = (ImageView) getActivity().findViewById(R.id.iv_play_style);
        ivPlayStyle.setOnClickListener(this);

        // Set up the current play style
        int plaStyle = prefs.getPlayStyle();
        int drawablePlayStyle = PlayStyle.getPlayStyleDrawable(plaStyle);
        ivPlayStyle.setImageResource(drawablePlayStyle);

    }

    public void setAlbumArt() {

        Track track = SharedPrefs.getInstance(getActivity()).getStoredTrack();

        if(null != track) {
            try {
                Bitmap bmp = App.getInstance().getHighResAlbumArt(track.getAlbumID(), getActivity());
                if(null != bmp) {
                    ivAlbumArt.setImageBitmap(bmp);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ivAlbumArt.setImageResource(R.drawable.no_album_art);
    }


    private void showSettings() {
        if(-1 != valueToAnimate) {
            ivAlbumArt.animate().translationXBy(valueToAnimate).start();
            rlSettings.animate().alpha(1).start();
            isControlPanelVisible = true;
        }
    }


    private void hideSettings() {
        if(-1 != valueToAnimate) {
            ivAlbumArt.animate().translationXBy(-valueToAnimate).start();
            rlSettings.animate().alpha(0).start();
            isControlPanelVisible = false;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_sound_settings : {
                try {
                    Intent aa = new Intent(android.provider.Settings.ACTION_SOUND_SETTINGS);
                    startActivityForResult(aa,0);
                } catch(Exception e) {
                    Toast.makeText(getActivity(), "Oops, some error occurred", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.iv_share : {
                app.shareTrack(getActivity());
                break;
            }
            case R.id.iv_play_style : {
                togglePlayStyle();
                break;
            }
        }
    }


    private void togglePlayStyle() {
        int playStyle = prefs.getPlayStyle();
        playStyle++;
        if (playStyle > 2) {
            playStyle = 0;
        }
        prefs.setPlayStyle(playStyle);
        int drawablePlayStyle = PlayStyle.getPlayStyleDrawable(playStyle);
        ivPlayStyle.setImageResource(drawablePlayStyle);
    }


    public void toggleOptionsForProductTour() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               ivAlbumArt.performClick();
            }
        }, 1000);

    }

}
