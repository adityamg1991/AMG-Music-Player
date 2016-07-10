package muzic.coffeemug.com.muzic.Streaming.Playback;

/**
 * Created by PAVILION on 6/13/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import muzic.coffeemug.com.muzic.Streaming.Activities.RecomsHomeActivity;
import muzic.coffeemug.com.muzic.Utilities.MasterPlaybackUtils;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;


/**
 * Created by PAVILION on 5/23/2016.
 */
public class StreamingController {

    private static StreamingController instance;
    private Context context;
    private TrackStore trackStore;
    private SharedPrefs prefs;


    private StreamingController(Context context) {
        this.context = context;
        trackStore = TrackStore.getInstance(context);
        prefs = SharedPrefs.getInstance(context);
    }


    public static StreamingController getInstance(Context context) {
        if (null == instance) {
            instance = new StreamingController(context);
        }
        return instance;
    }


    public void playTrack(String id) {

        Intent intent = new Intent(context, StreamingService.class);
        intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.PLAY_TRACK);
        intent.putExtra(MasterPlaybackUtils.Constants.SOUND_CLOUD_TRACK_ID, id);
        context.startService(intent);
    }


    public void pauseTrack() {
        Intent intent = new Intent(context, StreamingService.class);
        intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.PAUSE_TRACK);
        context.startService(intent);
    }


    /*public void resumeTrack() {
        Intent intent = new Intent(context, MasterPlaybackService.class);
        intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.RESUME_TRACK);
        context.startService(intent);
    }*/


    /*public void playNextTrack() {

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
    }*/


    /*public void moveTrackToPoint() {

        if (MasterPlaybackUtils.getInstance().isMasterPlaybackServiceRunning(context)) {
            Intent intent = new Intent(context, MasterPlaybackService.class);
            intent.putExtra(MasterPlaybackUtils.Constants.ACTION, MasterPlaybackUtils.Values.MOVE_TRACK);
            context.startService(intent);
        }
    }*/
}

