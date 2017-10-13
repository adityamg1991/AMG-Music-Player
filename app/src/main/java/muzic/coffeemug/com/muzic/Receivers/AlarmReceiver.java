package muzic.coffeemug.com.muzic.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import muzic.coffeemug.com.muzic.Service.DataService;

/**
 * Created by PAVILION on 7/12/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(LOG_TAG, "Alarm ran");
        context.startService(new Intent(context, DataService.class));

    }

}
