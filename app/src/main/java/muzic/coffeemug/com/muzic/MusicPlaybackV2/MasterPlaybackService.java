package muzic.coffeemug.com.muzic.MusicPlaybackV2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import muzic.coffeemug.com.muzic.Data.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;

public class MasterPlaybackService extends Service {


    private static final String LOG_TAG = "MasterPlaybackService";
    private MediaPlayer mediaPlayer;
    private static MuzicApplication muzicApplication;
    private static SharedPrefs prefs;
    private static TrackStore mTrackStore;



    public MasterPlaybackService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();

        muzicApplication = MuzicApplication.getInstance();
        prefs = SharedPrefs.getInstance(this);
        mTrackStore = TrackStore.getInstance();
        mediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != intent) {
            String strAction = getAction(intent);
            if (null != strAction) {
                if (MasterPlaybackUtils.Values.PLAY_TRACK.equals(strAction)) {
                    playSavedTrack();
                }
            } else {
                releaseResourcesAndStopSelf();
            }
        } else {
            releaseResourcesAndStopSelf();
        }

        return START_NOT_STICKY;
    }


    private void playSavedTrack() {
        Track trackToBePlayed = SharedPrefs.getInstance(this).getStoredTrack();
        if (null != trackToBePlayed) {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(trackToBePlayed.getData());
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void releaseResourcesAndStopSelf() {
        mediaPlayer.release();
        stopSelf();
    }


    private class MediaPlayerErrorListener implements MediaPlayer.OnErrorListener {
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(LOG_TAG, "Error : " + what);
            return false;
        }
    }


    private String getAction(Intent intent) {
        String strAction = null;
        if (intent.hasExtra(MasterPlaybackUtils.Constants.ACTION)) {
            strAction = intent.getStringExtra(MasterPlaybackUtils.Constants.ACTION);
        }
        return strAction;
    }
}
