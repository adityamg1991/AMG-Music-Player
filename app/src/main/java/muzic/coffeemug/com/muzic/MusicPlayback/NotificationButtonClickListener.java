package muzic.coffeemug.com.muzic.MusicPlayback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NotificationButtonClickListener extends BroadcastReceiver {
    public NotificationButtonClickListener() {
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if(null != intent) {

            String strAction = intent.getAction();
            Log.e("Aditya", strAction);
        }
    }


}
