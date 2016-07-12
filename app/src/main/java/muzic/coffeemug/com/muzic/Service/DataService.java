package muzic.coffeemug.com.muzic.Service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Utilities.APIConstants;
import muzic.coffeemug.com.muzic.Utilities.App;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;


public class DataService extends IntentService {

    private static final String LOG_TAG = "DataService";


    public DataService() {
        super("DataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "Alarm ran");

        final Gson gson = new Gson();
        final RequestQueue queue = App.getInstance().getRequestQueue();

        ArrayList<Track> trackList = App.getInstance().getDatabaseHelper()
                .getTracksStoredInDatabase();

        try {
            JSONArray jArray = new JSONArray();

            if (!trackList.isEmpty()) {

                for (Track track : trackList) {
                    String strObj = gson.toJson(track);
                    JSONObject obj = new JSONObject(strObj);
                    jArray.put(obj);
                }

                final JSONObject obj = new JSONObject();
                final String strIMEI = SharedPrefs.getInstance(this).getUUID();
                if (!TextUtils.isEmpty(strIMEI)) {

                    obj.put("imei", strIMEI);
                    obj.put("music_data", jArray);

                    Log.d("JSONObject to send", obj.toString());

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                            APIConstants.ADD_USER_DATA, obj, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error User Info", error.toString());
                        }
                    });
                    queue.add(request);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
