package muzic.coffeemug.com.muzic.Utilities;

import android.app.ActivityManager;
import android.content.Context;

import muzic.coffeemug.com.muzic.MusicPlayback.PlaybackService;
import muzic.coffeemug.com.muzic.Streaming.Playback.StreamingService;

/**
 * Created by PAVILION on 5/23/2016.
 */
public class MasterPlaybackUtils {

    private static MasterPlaybackUtils instance;

    private MasterPlaybackUtils(){}

    public static MasterPlaybackUtils getInstance() {
        if (null == instance) {
            instance = new MasterPlaybackUtils();
        }
        return instance;
    }

    public  boolean isMasterPlaybackServiceRunning(Context context) {
        final Class<?> serviceClass = PlaybackService.class;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public  boolean isMasterStreamingServiceRunning(Context context) {
        final Class<?> serviceClass = StreamingService.class;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public interface Constants {

        String ACTION = "ACTION";
        String SOUND_CLOUD_TRACK_ID = "SOUND_CLOUD_TRACK_ID";

    }


    public interface Values {

        String PLAY_TRACK = "PLAY_TRACK";
        String PAUSE_TRACK = "PAUSE_TRACK";
        String RESUME_TRACK = "RESUME_TRACK";
        String MOVE_TRACK = "MOVE_TRACK";

    }

}
