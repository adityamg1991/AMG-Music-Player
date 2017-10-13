package muzic.coffeemug.com.muzic.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.UUID;

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
    private static final String GSON_KEY = "GSON_KEY";
    private static final String KEY_HOME_LABEL = "KEY_HOME_LABEL";
    private static final String KEY_UUID = "KEY_UUID";
    private static final String KEY_ALARM_SET = "KEY_ALARM_SET";

    public interface PRODUCT_TOUR {
        String KEY_PLAY_TRACK_SCREEN = "KEY_PLAY_TRACK_SCREEN";
    }

    private SharedPrefs(Context context) {
        mContext = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public boolean getIsAlarmSet() {
        return sharedPreferences.getBoolean(KEY_ALARM_SET, false);
    }


    public void setIsAlarmSet(boolean b) {
        sharedPreferences.edit().putBoolean(KEY_ALARM_SET, b).commit();
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


    public void setUUID() {
        String savedUUID = getUUID();
        if (TextUtils.isEmpty(savedUUID)) {
            sharedPreferences.edit().putString(KEY_UUID, UUID.randomUUID().toString()).commit();
        }
    }


    public String getUUID() {
        return sharedPreferences.getString(KEY_UUID, null);
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


    public boolean getIntroKeyData(String key) {
        return sharedPreferences.getBoolean(key, true);
    }


    public void setIntroKeyDataFalse(String key) {
        sharedPreferences.edit().putBoolean(key, false).commit();
    }
}
