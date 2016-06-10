package muzic.coffeemug.com.muzic.Streaming;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MasterStreamingService extends Service
        implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener {

    private static final String LOG_TAG = "MasterStreamingService";
    private MediaPlayer mediaPlayer;


    public MasterStreamingService() {
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

        String link = "https://api.soundcloud.com/tracks/346346/stream?client_id=d734fcc82b1f04127dd928093ef9c5f3";
        startStreaming(link);

        return START_NOT_STICKY;
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
        mediaPlayer.start();
    }


    private class MediaPlayerErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(LOG_TAG, "Error : " + what + ", " + extra);
            return false;
        }
    }
}
