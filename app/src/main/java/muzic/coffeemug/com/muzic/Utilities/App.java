package muzic.coffeemug.com.muzic.Utilities;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Database.DatabaseHelper;

/**
 * Created by aditya on 01/09/15.
 */
public class App extends Application {

    private static App instance;
    private static DatabaseHelper databaseHelper;
    private static RequestQueue requestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = DatabaseHelper.getInstance(this);
        requestQueue = Volley.newRequestQueue(this);
    }


    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }


    public static App getInstance() {

        if (null == instance) {
            instance = new App();
        }
        return instance;
    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
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


    public static Bitmap getSongCoverArt(Context context, long album_id) {

        Bitmap bmp = null;

        Uri songCover = Uri.parse("content://media/external/audio/albumart");
        Uri uriSongCover = ContentUris.withAppendedId(songCover, album_id);

        ContentResolver res = context.getContentResolver();
        try {
            InputStream in = res.openInputStream(uriSongCover);
            bmp = BitmapFactory.decodeStream(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp;
    }


    public Bitmap getAlbumArt(String _id, Activity activity) throws Exception {

        Bitmap bmp = null;

        Cursor cursor = activity.managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{_id},
                null);

        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
            bmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        }

        return bmp;
    }


    public Bitmap getHighResAlbumArt(String _id, Activity activity) throws Exception {

        Bitmap bmp = null;

        Cursor cursor = activity.managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{_id},
                null);

        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmp = BitmapFactory.decodeFile(path, bmOptions);
        }

        return bmp;
    }

    /**
     * Shares the track you pass as a parameter
     *
     * @param mContext
     * @param mTrack
     */
    public void shareTrack(Context mContext, Track mTrack) {

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + mTrack.getData()));
        mContext.startActivity(Intent.createChooser(share, "Share " + mTrack.getTitle()));
    }

    /**
     * Shares the stored track From SharedPrefs
     *
     * @param mContext
     */
    public void shareTrack(Context mContext) {

        SharedPrefs prefs = SharedPrefs.getInstance(mContext);
        Track track = prefs.getStoredTrack();
        if (null != track) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("audio/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + track.getData()));
            mContext.startActivity(Intent.createChooser(share, "Share " + track.getTitle()));
        } else {
            Toast.makeText(mContext, "Oops, some error occurred", Toast.LENGTH_SHORT).show();
        }
    }


    public String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getTimeString(long durationInSec) {

        String str = "";

        try {
            String strMins = String.valueOf(durationInSec / 60);
            if(strMins.length() == 1) {
                strMins = "0" + strMins;
            }

            String strSecs = String.valueOf(durationInSec % 60);
            if(strSecs.length() == 1) {
                strSecs = "0" + strSecs;
            }

            str = strMins + ":" + strSecs;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return str;
    }
}
