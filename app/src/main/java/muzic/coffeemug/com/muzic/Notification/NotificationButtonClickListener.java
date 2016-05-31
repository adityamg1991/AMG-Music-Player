package muzic.coffeemug.com.muzic.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import muzic.coffeemug.com.muzic.Utilities.AppConstants;

/**
 * Created by aditya on 1/6/16.
 */
public class NotificationButtonClickListener extends BroadcastReceiver {

    public NotificationButtonClickListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null != intent) {

            String strAction = intent.getAction();

            if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_PLAY_PAUSE)) {
                // Play/Pause current Track
            } else if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_NEXT_TRACK)) {
                // Play the next Track
            } else if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_EXIT)) {
                // Exit the music playback service
            }
        }
    }

}
