package muzic.coffeemug.com.muzic;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Data.Track;

/**
 * Created by aditya on 01/09/15.
 */
public class MuzicApplication extends Application {

    private static MuzicApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = new MuzicApplication();
    }


    public static MuzicApplication getInstance() {

        if(null == instance) {
            instance = new MuzicApplication();
        }
        return instance;
    }


    /**
     * This method runs on the main thread. Put call to this method in AsyncTask to avoid ANR.
     * @return
     */
    public ArrayList<Track> getMusicFiles(Context context) {

        ArrayList<Track> musicList = new ArrayList<Track>();

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.AlbumColumns.ALBUM
        };

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = musicResolver.query(musicUri, projection, selection, null, "UPPER (" + MediaStore.Audio.Media.TITLE + ") ASC");

        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String id = cursor.getString(0);
            String artist = cursor.getString(1);
            String title = cursor.getString(2);
            String data = cursor.getString(3);
            String name = cursor.getString(4);
            String duration = cursor.getString(5);
            String albumName = cursor.getString(6);

            Track track = new Track(id, artist, title, data, name, duration, albumName);
            musicList.add(track);
        }

        cursor.close();

        return musicList;
    }


    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }


    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }


    public static int getScreenHeight(Context con) {

        WindowManager wm = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        return height;
    }


    public void showToast(String msg, Context con) {
        Toast.makeText(con, msg, Toast.LENGTH_SHORT).show();
    }
}
