package muzic.coffeemug.com.muzic.Utilities;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import muzic.coffeemug.com.muzic.MusicPlayback.PlaybackController;

/**
 * Created by PAVILION on 6/10/2016.
 */
public class MuzicAudioFocus {

    private static final String LOG_TAG = "MuzicAudioFocus";
    private Context context;
    private static MuzicAudioFocus instance;

    private AudioManager managerAudio;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;


    private MuzicAudioFocus(Context context) {
        this.context = context;
        managerAudio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS:
                        Log.e(LOG_TAG, "Audio Focus Loss");
                        pauseTrackIfPlaying();
                        break;
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        Log.e(LOG_TAG, "Audio Focus Loss Transient");
                        pauseTrackIfPlaying();
                        break;
                }
            }
        };

    }


    private void pauseTrackIfPlaying() {

        if (MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(context)) {
            PlaybackController.getInstance(context).pauseTrack();
        }

    }


    public static MuzicAudioFocus getInstance(Context context) {
        if (null == instance) {
            instance = new MuzicAudioFocus(context);
        }
        return instance;
    }


    public boolean getAudioFocus() {

        int result = managerAudio.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        } else {
            return false;
        }

    }


    public void abandon() {
        managerAudio.abandonAudioFocus(mOnAudioFocusChangeListener);
    }

}
