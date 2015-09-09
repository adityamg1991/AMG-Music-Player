package muzic.coffeemug.com.muzic.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by aditya on 08/09/15.
 */
public class SharedPrefs {

    private static SharedPrefs instance;
    private static SharedPreferences sharedPreferences;
    private Gson gson = new Gson();
    private final String GSON_KEY = "gson_key";


    private SharedPrefs(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static SharedPrefs getInstance(Context context) {

        if(null == instance) {
            instance = new SharedPrefs(context);
        }
        return instance;
    }


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

}
