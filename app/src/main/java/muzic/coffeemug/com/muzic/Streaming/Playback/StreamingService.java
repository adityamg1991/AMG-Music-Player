package muzic.coffeemug.com.muzic.Streaming.Playback;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Utilities.MasterPlaybackUtils;
import muzic.coffeemug.com.muzic.Utilities.MuzicAudioFocus;

public class StreamingService extends Service
        implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener {

    private static final String LOG_TAG = "MasterStreamingService";
    private MediaPlayer mediaPlayer;


    public StreamingService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener(new MediaPlayerErrorListener());
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (null != intent && intent.hasExtra(MasterPlaybackUtils.Constants.ACTION)) {
            String strAction = intent.getStringExtra(MasterPlaybackUtils.Constants.ACTION);
            if (null != strAction) {
                if (strAction.equals(MasterPlaybackUtils.Values.PLAY_TRACK)) {
                    playSoundCloudTrack(intent.getStringExtra(MasterPlaybackUtils.Constants.SOUND_CLOUD_TRACK_ID));
                } else if (strAction.equals(MasterPlaybackUtils.Values.PAUSE_TRACK)) {
                    releaseResourcesAndStopSelf();
                }

            }
        }

        return START_NOT_STICKY;
    }


    private void playSoundCloudTrack(String soundCloudTrackId) {

        String link = AppConstants.SoundCloud.STREAM_URL_HEADER + soundCloudTrackId + AppConstants.SoundCloud.STREAM_URL_FOOTER;
        Log.d(LOG_TAG, "Streaming Music : " + link);
        startStreaming(link);

    }


    private void startStreaming(String link) {

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(link);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }


    @Override
    public void onCompletion(MediaPlayer mp) {

    }


    @Override
    public void onPrepared(MediaPlayer mp) {

        if (MuzicAudioFocus.getInstance(StreamingService.this).getAudioFocus()) {
            mediaPlayer.start();
        }

    }


    private class MediaPlayerErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(LOG_TAG, "Error : " + what + ", " + extra);
            return false;
        }
    }


    private void releaseResourcesAndStopSelf() {

        Log.d(LOG_TAG, "Pausing Track, Stopping Service");
        if (null != mediaPlayer) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        //sendPlaybackStatusEvent(false);
        //stopTimer();
        //stopForeground(removeNotification);
        stopSelf();
        MuzicAudioFocus.getInstance(this).abandon();
        //unregisterReceiver(headSetReceiver);

    }

}
