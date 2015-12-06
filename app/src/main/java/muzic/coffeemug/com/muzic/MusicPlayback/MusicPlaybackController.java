package muzic.coffeemug.com.muzic.MusicPlayback;

import android.content.Context;
import android.content.Intent;

import muzic.coffeemug.com.muzic.Data.Track;

/**
 * Created by PAVILION on 12/5/2015.
 */
public class MusicPlaybackController {

    private static MusicPlaybackController instance;

    private MusicPlaybackController() {}

    public static MusicPlaybackController getInstance() {

        if(null == instance) {
            instance = new MusicPlaybackController();
        }
        return instance;
    }


    public void playTrack(Track track, Context context) {

        Intent serviceIntent = new Intent(context, MusicPlaybackService.class);
        serviceIntent.putExtra(MusicPlaybackConstants.TRACK_TO_BE_PLAYED, track);
        context.startService(serviceIntent);
    }


}
