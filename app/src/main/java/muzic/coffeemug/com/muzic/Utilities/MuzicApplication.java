package muzic.coffeemug.com.muzic.Utilities;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by aditya on 01/09/15.
 */
public class MuzicApplication extends Application {

    private static MuzicApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static MuzicApplication getInstance() {

        if(null == instance) {
            instance = new MuzicApplication();
        }
        return instance;
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


    public static Bitmap getSongCoverArt(Context context, long album_id){

        Bitmap bmp = null;

        Uri songCover = Uri.parse("content://media/external/audio/albumart");
        Uri uriSongCover = ContentUris.withAppendedId(songCover, album_id);

        ContentResolver res = context.getContentResolver();
        try {
            InputStream in = res.openInputStream(uriSongCover);
            bmp = BitmapFactory.decodeStream(in);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return bmp;
    }


    public Bitmap getAlbumArt(String _id, Activity activity) throws Exception{

        Bitmap bmp = null;

        Cursor cursor = activity.managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{_id},
                null);

        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
            bmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        }

        return bmp;
    }


    public Bitmap getHighResAlbumArt(String _id, Activity activity) throws Exception{

        Bitmap bmp = null;

        Cursor cursor = activity.managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{_id},
                null);

        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmp = BitmapFactory.decodeFile(path,bmOptions);
        }

        return bmp;
    }
}