package muzic.coffeemug.com.muzic.MusicPlayback;

import android.content.Context;
import android.content.Intent;

/**
 * Created by PAVILION on 12/5/2015.
 */
public class MusicPlaybackController {

    private static MusicPlaybackController instance;
    private Context mContext;
    private static TrackPlayingObserver trackPlayingObserver;

    private MusicPlaybackController(Context con) {
        mContext = con;
    }

    public static MusicPlaybackController getInstance(Context con) {

        if(null == instance) {
            instance = new MusicPlaybackController(con);
            trackPlayingObserver = new MusicPlaybackService();
        }
        return instance;
    }


    public void playTrack(Context c) {

        Intent serviceIntent = new Intent(mContext, MusicPlaybackService.class);
        serviceIntent.putExtra(MusicPlaybackConstants.ACTION, MusicPlaybackConstants.ACTION_PLAY);
        mContext.startService(serviceIntent);
    }


    public void resumeTrack() {

        Intent serviceIntent = new Intent(mContext, MusicPlaybackService.class);
        serviceIntent.putExtra(MusicPlaybackConstants.ACTION, MusicPlaybackConstants.ACTION_CONTINUE);
        mContext.startService(serviceIntent);
    }

    public void pauseTrack() {

        Intent serviceIntent = new Intent(mContext, MusicPlaybackService.class);
        serviceIntent.putExtra(MusicPlaybackConstants.ACTION, MusicPlaybackConstants.ACTION_PAUSE);
        mContext.startService(serviceIntent);
    }


    public boolean getIsPlaying() {
        return trackPlayingObserver.isTrackPlaying();
    }


    public interface TrackPlayingObserver {

        boolean isTrackPlaying();
    }

}
