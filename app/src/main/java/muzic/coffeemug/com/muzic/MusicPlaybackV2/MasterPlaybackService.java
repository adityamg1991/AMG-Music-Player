package muzic.coffeemug.com.muzic.MusicPlaybackV2;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import muzic.coffeemug.com.muzic.Events.PlaybackStatusEvent;
import muzic.coffeemug.com.muzic.Notification.NotificationsHub;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Events.TrackProgressEvent;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;

public class MasterPlaybackService extends Service {


    private static final String LOG_TAG = "MasterPlaybackService";
    private MediaPlayer mediaPlayer;
    private MuzicApplication muzicApplication;
    private SharedPrefs prefs;
    private TrackStore mTrackStore;
    private Handler handlerProgress;
    private RunnableProgress runnableProgress;
    private MasterPlaybackController masterPlaybackController;


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

        handlerProgress = new Handler();
        runnableProgress = new RunnableProgress();
        mediaPlayer = new MediaPlayer();

        masterPlaybackController = MasterPlaybackController.getInstance(this);
        muzicApplication = MuzicApplication.getInstance();
        prefs = SharedPrefs.getInstance(this);
        mTrackStore = TrackStore.getInstance(this);
        mediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                masterPlaybackController.playNextTrack();
            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != intent) {
            String strAction = getAction(intent);
            if (null != strAction) {
                if (MasterPlaybackUtils.Values.PLAY_TRACK.equals(strAction)) {
                    playSavedTrack(false);
                } else if (MasterPlaybackUtils.Values.PAUSE_TRACK.equals(strAction)) {
                    releaseResourcesAndStopSelf(true);
                } else if (MasterPlaybackUtils.Values.RESUME_TRACK.equals(strAction)) {
                    playSavedTrack(true);
                } else if (MasterPlaybackUtils.Values.MOVE_TRACK.equals(strAction)) {
                    moveTrack();
                }
            } else {
                releaseResourcesAndStopSelf(true);
            }
        } else {
            releaseResourcesAndStopSelf(true);
        }

        return START_NOT_STICKY;
    }


    private void moveTrack() {

        final int progress = prefs.getTrackProgress();

        if (AppConstants.SharedPref.EMPTY_PROGRESS != progress) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(progress * 1000);
            }
        }

    }


    private void moveSeekerToLastKnownPosition() {
        int position = prefs.getTrackProgress();
        if (AppConstants.SharedPref.EMPTY_PROGRESS != position) {
            mediaPlayer.seekTo(position * 1000);
        }
    }


    private void playSavedTrack(final boolean seek) {

        Track trackToBePlayed = SharedPrefs.getInstance(this).getStoredTrack();

        if (!seek) {
            // This is a Track being played from the beginning. Save in Database.
            muzicApplication.getDatabaseHelper().saveTrackInDatabase();
        }

        if (null == trackToBePlayed) {
            trackToBePlayed = mTrackStore.getFirstTrack();
            prefs.storeTrack(trackToBePlayed);
        }

        if (null != trackToBePlayed) {
            try {

                Notification notification = NotificationsHub.getInstance(this).
                        getTrackNotification(trackToBePlayed);

                startForeground(AppConstants.MusicPlayback.TRACK_NOTI_ID, notification);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(trackToBePlayed.getData());
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        sendPlaybackStatusEvent(true);
                        startTimer();
                        if (seek) {
                            moveSeekerToLastKnownPosition();
                        }
                        mp.start();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void releaseResourcesAndStopSelf(boolean removeNotification) {

        if (null != mediaPlayer) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        sendPlaybackStatusEvent(false);
        stopTimer();
        Toast.makeText(this, "Music stopped", Toast.LENGTH_SHORT).show();
        stopForeground(removeNotification);
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


    private void startTimer() {
        handlerProgress.postDelayed(runnableProgress, 0);
    }


    private class RunnableProgress implements Runnable {

        @Override
        public void run() {
            int progress = mediaPlayer.getCurrentPosition() / 1000;
            prefs.saveTrackProgress(progress);
            sendProgressEvent(progress);
            handlerProgress.postDelayed(this, 1000);
        }
    }


    private void sendProgressEvent(int progress) {
        EventBus.getDefault().post(new TrackProgressEvent(progress));
    }


    private void stopTimer() {
        handlerProgress.removeCallbacks(runnableProgress);
        handlerProgress = null;
        runnableProgress = null;
    }


    private void sendPlaybackStatusEvent(boolean playing) {
        EventBus.getDefault().post(new PlaybackStatusEvent(playing));
    }
}
