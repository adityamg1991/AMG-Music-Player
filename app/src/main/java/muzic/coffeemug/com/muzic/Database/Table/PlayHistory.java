package muzic.coffeemug.com.muzic.Database.Table;

/**
 * Created by aditya on 4/6/16.
 */

public class PlayHistory {

    /**
     * Table name
     */
    public static final String TABLE_NAME = "play_history_object";

    /**
     * Columns
     */
    public static final String _ID = "_id";
    // ID in Track.class
    public static final String MEDIA_ID = "media_id";
    public static final String TRACK_NAME = "track_name";
    public static final String TIMES_PLAYED = "times_played";
    // In Unix Timestamp
    public static final String LAST_PLAYED = "last_played";


    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " ( " + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + MEDIA_ID + " TEXT, " +
            TRACK_NAME + " Text, " + TIMES_PLAYED + " INTEGER, " + LAST_PLAYED + " INTEGER )";

}


