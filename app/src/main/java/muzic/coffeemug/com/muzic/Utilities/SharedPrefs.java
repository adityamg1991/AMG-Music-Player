package muzic.coffeemug.com.muzic.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;


/**
 * Created by aditya on 08/09/15.
 */
public class SharedPrefs {


    private static SharedPrefs instance;
    private static SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    private Context mContext;

    private static final String KEY_PLAY_STYLE = "KEY_PLAY_STYLE";
    private static final String KEY_TRACK_PROGRESS = "KEY_TRACK_PROGRESS";
    private static final String GSON_KEY = "gson_key";
    private static final String KEY_HOME_LABEL = "KEY_HOME_LABEL";

    private SharedPrefs(Context context) {
        mContext = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public static SharedPrefs getInstance(Context context) {

        if (null == instance) {
            instance = new SharedPrefs(context);
        }
        return instance;
    }

    /**
     * Stores the track info in SharedPrefs.
     *
     * @param track
     */
    public void storeTrack(Track track) {

        String str = gson.toJson(track);
        sharedPreferences.edit().putString(GSON_KEY, str).commit();
    }


    /**
     * Returned track can be NULL. Buyer Beware !
     *
     * @return
     */
    public Track getStoredTrack() {

        String str = sharedPreferences.getString(GSON_KEY, null);
        if (null == str) {
            return null;
        }

        return gson.fromJson(str, Track.class);
    }

    /**
     * Progress is in seconds.
     * @param progress
     */
    public void saveTrackProgress(int progress) {
        sharedPreferences.edit().putInt(KEY_TRACK_PROGRESS, progress).commit();
    }


    public int getTrackProgress() {
        return sharedPreferences.getInt(KEY_TRACK_PROGRESS, AppConstants.SharedPref.EMPTY_PROGRESS);
    }


    public void setPlayStyle(int style) {
        sharedPreferences.edit().putInt(KEY_PLAY_STYLE, style).commit();
    }


    public int getPlayStyle() {
        return sharedPreferences.getInt(KEY_PLAY_STYLE, PlayStyle.REPEAT_ALL);
    }


    public void setHomeLabel(String strLabel) {
        sharedPreferences.edit().putString(KEY_HOME_LABEL, strLabel).commit();
    }


    public String getHomeLabel() {
        return sharedPreferences.getString(KEY_HOME_LABEL,
                mContext.getString(R.string.track_list_activity_label));
    }
}
