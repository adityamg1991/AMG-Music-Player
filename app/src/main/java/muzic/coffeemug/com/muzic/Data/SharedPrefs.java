package muzic.coffeemug.com.muzic.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import muzic.coffeemug.com.muzic.MusicPlayback.MusicPlaybackController;

/**
 * Created by aditya on 08/09/15.
 */
public class SharedPrefs {

    private static SharedPrefs instance;
    private static SharedPreferences sharedPreferences;
    private Gson gson = new Gson();
    private final String GSON_KEY = "gson_key";
    private Context mContext;

    private static TrackCurrentPosition trackCurrentPosition;


    private SharedPrefs(Context context){
        mContext = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public static SharedPrefs getInstance(Context context) {

        if(null == instance) {
            instance = new SharedPrefs(context);
            trackCurrentPosition = new TrackCurrentPosition();
        }
        return instance;
    }

    /**
     * Stores the track info in SharedPrefs.
     * @param track
     */
    public void storeTrack(Track track) {

        String str = gson.toJson(track);
        sharedPreferences.edit().putString(GSON_KEY, str).commit();
    }


    /**
     * Returned track can be NULL. Buyer Beware !
     * @return
     */
    public Track getStoredTrack() {

        String str = sharedPreferences.getString(GSON_KEY, null);
        if(null == str) {
            return null;
        }

        return gson.fromJson(str, Track.class);
    }


    public void saveTrackCurrentPosition(int pos) {
        trackCurrentPosition.saveTrackCurrentPosition(pos);
    }

    /**
     * In case no song is present, -1 will be returned.
     * @return
     */
    public int getTrackCurrentPosition() {
        return trackCurrentPosition.getTrackCurrentPosition();
    }


    private static class TrackCurrentPosition {

        private static final String KEY = "track_current_pos_key";

        protected void saveTrackCurrentPosition(int currentPosition) {
            sharedPreferences.edit().putInt(KEY, currentPosition).commit();
        }

        protected int getTrackCurrentPosition() {
            return sharedPreferences.getInt(KEY, -1);
        }

    }

}
