package muzic.coffeemug.com.muzic.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import muzic.coffeemug.com.muzic.MusicPlayback.MasterPlaybackController;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;

/**
 * Created by aditya on 1/6/16.
 */
public class NotificationButtonClickListener extends BroadcastReceiver {

    public NotificationButtonClickListener() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (null != intent) {

            final String strAction = intent.getAction();
            final MasterPlaybackController controller = MasterPlaybackController.getInstance(context);
            final NotificationsHub notificationsHub = NotificationsHub.getInstance(context);
            final SharedPrefs prefs = SharedPrefs.getInstance(context);
            final NotificationManager managerNotification =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


            if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_PLAY)) {

                controller.resumeTrack();

            } else if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_PAUSE)) {

                controller.pauseTrack();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Notification notification = notificationsHub.getPausedTrackNotification(prefs.getStoredTrack());
                        managerNotification.notify(AppConstants.MusicPlayback.TRACK_NOTI_ID, notification);
                    }
                }, 1000);

            } else if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_NEXT_TRACK)) {

                // Play the next Track
                controller.playNextTrack();

            } else if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_EXIT)) {

                // Exit the music playback service
                controller.pauseTrack();

            } else if (strAction.equalsIgnoreCase(AppConstants.MusicPlayback.NOTIFICATION_REMOVE_NOTI)) {
                managerNotification.cancelAll();
            }
        }
    }

}
