package muzic.coffeemug.com.muzic.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Database.Table.PlayHistory;
import muzic.coffeemug.com.muzic.Store.TrackStore;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;

/**
 * Created by aditya on 4/6/16.
 */
public class DatabaseHelper {

    private static final String DATABASE_NAME = "MUZIC_DATABASE";
    private static final String LOG_TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;
    private static PrivateDatabaseHelper innerInstance;
    private static SQLiteDatabase database;
    private SharedPrefs prefs;
    private TrackStore trackStore;


    private DatabaseHelper(Context context){
        prefs = SharedPrefs.getInstance(context);
        trackStore = TrackStore.getInstance(context);
    }


    public static DatabaseHelper getInstance(Context con) {

        if(null == innerInstance) {
            innerInstance = new PrivateDatabaseHelper(con, DATABASE_NAME, null, DATABASE_VERSION);
        }

        if(null == database) {
            database = innerInstance.getWritableDatabase();
        }

        if(null == instance) {
            instance = new DatabaseHelper(con);
        }

        return instance;
    }


    private static class PrivateDatabaseHelper extends SQLiteOpenHelper {

        public PrivateDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d(LOG_TAG, "Creating table : " + PlayHistory.CREATE_TABLE);
            db.execSQL(PlayHistory.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    public void saveTrackInDatabase() {

        Log.d(LOG_TAG, "saveTrackInDatabase() Hit");

        final Track track = prefs.getStoredTrack();

        if (isTrackPresentInDatabase(track)) {
            updateTrackInDatabase(track);
        } else {
            insertTrackInDatabase(track);
        }

    }


    private void updateTrackInDatabase(Track track) {

        Log.d(LOG_TAG, "Old Track : Updating in Database");

        final String trackID = track.getID();
        final String getFrequencyQuery = "SELECT * FROM " + PlayHistory.TABLE_NAME + " WHERE " + PlayHistory
                .MEDIA_ID  + " LIKE " + "'" + trackID + "'";
        Log.d(LOG_TAG, getFrequencyQuery);
        final Cursor cursor = database.rawQuery(getFrequencyQuery, null);

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            // Updating frequency
            int timePlayed = cursor.getInt(cursor.getColumnIndex(PlayHistory.TIMES_PLAYED));
            timePlayed++;

            final long currentTime = System.currentTimeMillis();

            final String updateQuery = "UPDATE " + PlayHistory.TABLE_NAME + " SET " +
                    PlayHistory.TIMES_PLAYED + " = " + timePlayed + ", " +
                    PlayHistory.LAST_PLAYED + " = " + currentTime +
                    " WHERE " + PlayHistory.MEDIA_ID + " LIKE " + "'" + trackID + "'";

            Log.d(LOG_TAG, updateQuery);
            database.execSQL(updateQuery);
        }
    }


    private void insertTrackInDatabase(Track track) {

        Log.d(LOG_TAG, "New Track : Storing in Database");

        ContentValues cv = new ContentValues();
        cv.put(PlayHistory.MEDIA_ID, track.getID());
        cv.put(PlayHistory.TRACK_NAME, track.getTitle());
        cv.put(PlayHistory.TIMES_PLAYED, 1);
        cv.put(PlayHistory.LAST_PLAYED, System.currentTimeMillis());

        Log.d(LOG_TAG, "Content Values to be put : " + cv.toString());
        database.insert(PlayHistory.TABLE_NAME, null, cv);

    }


    private boolean isTrackPresentInDatabase(Track track) {

        final String id = track.getID();

        final String query = "SELECT " + PlayHistory._ID + " FROM " + PlayHistory.TABLE_NAME
                + " WHERE " + PlayHistory.MEDIA_ID + " LIKE '" + id + "'";

        Log.d(LOG_TAG, "Media Check Query : " + query);

        final Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() <= 0) {
            cursor.close();
            Log.d(LOG_TAG, "Track Absent in Database");
            return false;
        }

        cursor.close();
        Log.d(LOG_TAG, "Track Present in Database");
        return true;
    }


    public ArrayList<Track> getTracksStoredInDatabase() {

        final ArrayList<Track> list = new ArrayList<>();
        final String query = "SELECT * FROM " + PlayHistory.TABLE_NAME;
        Log.d(LOG_TAG, "Get all tracks from DB Query : " + query);

        Cursor cursor = database.rawQuery(query, null);
        try {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {

                final String strMediaId = cursor.getString(cursor.getColumnIndex(PlayHistory.MEDIA_ID));
                final int timesPlayed = cursor.getInt(cursor.getColumnIndex(PlayHistory.TIMES_PLAYED));
                final long lastPlayed = cursor.getLong(cursor.getColumnIndex(PlayHistory.LAST_PLAYED));

                Track track = trackStore.getTrackByMediaID(strMediaId);
                track.setTimesPlayed(timesPlayed);
                track.setLastPlayed(lastPlayed);

                list.add(track);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        return list;

    }

}
