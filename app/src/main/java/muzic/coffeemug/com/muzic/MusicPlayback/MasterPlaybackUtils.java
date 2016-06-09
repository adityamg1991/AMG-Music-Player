package muzic.coffeemug.com.muzic.MusicPlayback;

import android.app.ActivityManager;
import android.content.Context;

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
        final Class<?> serviceClass = MasterPlaybackService.class;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    interface Constants {

        String ACTION = "ACTION";

    }


    interface Values {

        String PLAY_TRACK = "PLAY_TRACK";
        String PAUSE_TRACK = "PAUSE_TRACK";
        String RESUME_TRACK = "RESUME_TRACK";
        String MOVE_TRACK = "MOVE_TRACK";

    }

}
