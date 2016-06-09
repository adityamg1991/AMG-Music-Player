package muzic.coffeemug.com.muzic.Notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import muzic.coffeemug.com.muzic.Activities.PlayTrackActivity;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.R;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Utilities.MuzicApplication;

/**
 * Created by aditya on 31/5/16.
 */
public class NotificationsHub {

    private static NotificationsHub instance;
    private Context context;
    private static MuzicApplication muzicApplication;


    private NotificationsHub(Context context) {
        this.context = context;
    }


    public static NotificationsHub getInstance(Context context) {

        if (null == instance) {
            instance = new NotificationsHub(context);
        }

        if (null == muzicApplication) {
            muzicApplication = MuzicApplication.getInstance();
        }

        return instance;
    }


    public Notification getTrackNotification(Track track) {

        // Setting up the view of Notification
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);

        // Adding relevant data to notification
        Bitmap bmp = muzicApplication.getSongCoverArt(context, Long.parseLong(track.getAlbumID()));
        if (null == bmp) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_album_art_black_small);
        }
        remoteViews.setImageViewBitmap(R.id.iv_noti_album_art, bmp);
        remoteViews.setTextViewText(R.id.noti_track_name, track.getTitle());
        remoteViews.setTextViewText(R.id.noti_artist_name, track.getArtist());

        // Setting up clicks on Notification (Next track)
        Intent nextTrackIntent = new Intent(context, NotificationButtonClickListener.class);
        nextTrackIntent.setAction(AppConstants.MusicPlayback.NOTIFICATION_NEXT_TRACK);
        PendingIntent piNextTrack = PendingIntent.getBroadcast(context, 0, nextTrackIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_next, piNextTrack);

        // Setting up clicks on Notification (Play/Pause)
        Intent playIntent = new Intent(context, NotificationButtonClickListener.class);
        playIntent.setAction(AppConstants.MusicPlayback.NOTIFICATION_PAUSE);
        PendingIntent piPlayPause = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_play_pause, piPlayPause);

        Intent exitIntent = new Intent(context, NotificationButtonClickListener.class);
        exitIntent.setAction(AppConstants.MusicPlayback.NOTIFICATION_EXIT);
        PendingIntent piExit = PendingIntent.getBroadcast(context, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_exit, piExit);

        Intent notificationIntent = new Intent(context, PlayTrackActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent).build();

        return notification;

    }


    public Notification getPausedTrackNotification(Track track) {

        // Setting up the view of Notification
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout_paused);

        // Adding relevant data to notification
        Bitmap bmp = muzicApplication.getSongCoverArt(context, Long.parseLong(track.getAlbumID()));
        if (null == bmp) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_album_art_black_small);
        }
        remoteViews.setImageViewBitmap(R.id.iv_noti_album_art, bmp);
        remoteViews.setTextViewText(R.id.noti_track_name, track.getTitle());
        remoteViews.setTextViewText(R.id.noti_artist_name, track.getArtist());

        // Setting up clicks on Notification (Next track)
        Intent nextTrackIntent = new Intent(context, NotificationButtonClickListener.class);
        nextTrackIntent.setAction(AppConstants.MusicPlayback.NOTIFICATION_NEXT_TRACK);
        PendingIntent piNextTrack = PendingIntent.getBroadcast(context, 0, nextTrackIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_next, piNextTrack);

        // Setting up clicks on Notification (Play/Pause)
        Intent playIntent = new Intent(context, NotificationButtonClickListener.class);
        playIntent.setAction(AppConstants.MusicPlayback.NOTIFICATION_PLAY);
        PendingIntent piPlayPause = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_play_pause, piPlayPause);

        Intent exitIntent = new Intent(context, NotificationButtonClickListener.class);
        exitIntent.setAction(AppConstants.MusicPlayback.NOTIFICATION_REMOVE_NOTI);
        PendingIntent piExit = PendingIntent.getBroadcast(context, 0, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.noti_exit, piExit);

        Intent notificationIntent = new Intent(context, PlayTrackActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent).build();

        return notification;

    }

}
