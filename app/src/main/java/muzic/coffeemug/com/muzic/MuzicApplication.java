package muzic.coffeemug.com.muzic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.provider.MediaStore;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

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
}
