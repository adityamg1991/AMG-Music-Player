package muzic.coffeemug.com.muzic.MusicPlayback;

import android.content.Context;
import android.content.Intent;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.PlayStyle;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;

/**
 * Created by PAVILION on 5/23/2016.
 */
public class MasterPlaybackController {

    private static MasterPlaybackController instance;
    private Context context;
    private TrackStore trackStore;
    private SharedPrefs prefs;


    private MasterPlaybackController(Context context) {
        this.context = context;
        trackStore = TrackStore.getInstance(context);
        prefs = SharedPrefs.getInstance(context);
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


    public void playNextTrack() {

        int playStyle = prefs.getPlayStyle();
        Track track = null;

        if (PlayStyle.REPEAT_ALL == playStyle) {
            track = trackStore.getNextLinearTrack();
        } else if (PlayStyle.REPEAT_ONE == playStyle) {
            // Nothing to do, play the same track again
        } else if (PlayStyle.SHUFFLE == playStyle) {
            track = trackStore.getNextRandomTrack();
        }

        if (null != track) {
            prefs.storeTrack(track);
        }

        playTrack();
    }


    public void moveTrackToPoint() {

        if (MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(context)) {
            Intent intent = new Intent(context, MasterPlaybackService.class);
            intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.MOVE_TRACK);
            context.startService(intent);
        }
    }
}