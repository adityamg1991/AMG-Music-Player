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


    public void playTrack() {

        Intent intent = new Intent(context, MasterPlaybackService.class);
        intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.PLAY_TRACK);
        context.startService(intent);

    }


    public void pauseTrack() {
        Intent intent = new Intent(context, MasterPlaybackService.class);
        intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.PAUSE_TRACK);
        context.startService(intent);
    }


    public void resumeTrack() {
        Intent intent = new Intent(context, MasterPlaybackService.class);
        intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.RESUME_TRACK);
        context.startService(intent);
    }
}
