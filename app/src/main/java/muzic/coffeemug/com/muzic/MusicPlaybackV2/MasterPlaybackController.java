package muzic.coffeemug.com.muzic.MusicPlaybackV2;

import android.content.Context;
import android.content.Intent;

import muzic.coffeemug.com.muzic.Data.Track;

/**
 * Created by PAVILION on 5/23/2016.
 */
public class MasterPlaybackController {

    private static MasterPlaybackController instance;
    private Context context;


    private MasterPlaybackController(Context context) {
        this.context = context;
    }


    public static MasterPlaybackController getInstance(Context context) {
        if (null == instance) {
            instance = new MasterPlaybackController(context);
        }
        return instance;
    }


    public void saveAndPlayTrack(Track track) {

        if (MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(context)) {
            // Already some song is playing.
        } else {
            // No song is playing, start afresh
            context.startService(new Intent(context, MasterPlaybackService.class));
        }

    }
}
