package muzic.coffeemug.com.muzic.Store;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;

import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Utilities.Constants;
import muzic.coffeemug.com.muzic.Data.Track;

/**
 * Created by aditya on 11/09/15.
 */
public class TrackStore {

    private static TrackStore instance;
    private ArrayList<Track> mTrackList;
    private ResultReceiver resultReceiver;


    public static TrackStore getInstance() {

        if(null == instance) {
            instance = new TrackStore();
        }
        return instance;
    }


    public void readyTracks(final Context context, ResultReceiver rr) {

        resultReceiver = rr;

        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... voids) {

                mTrackList = new ArrayList<Track>();

                String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

                String[] projection = {
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.AlbumColumns.ALBUM,
                        MediaStore.Audio.Media.ALBUM_ID
                };

                ContentResolver musicResolver = context.getContentResolver();
                Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = musicResolver.
                        query(musicUri, projection, selection, null, "UPPER (" + MediaStore.Audio.Media.TITLE + ") ASC");

                for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                    String id = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String title = cursor.getString(2);
                    String data = cursor.getString(3);
                    String name = cursor.getString(4);
                    String duration = cursor.getString(5);
                    String albumName = cursor.getString(6);
                    String albumID = cursor.getString(7);

                    Track track = new Track(id, artist, title, data, name, duration, albumName, albumID);
                    mTrackList.add(track);
                }

                cursor.close();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.TRACK_LIST, mTrackList);
                resultReceiver.send(Activity.RESULT_OK, bundle);

                return null;
            }
        }.execute();
    }


    /**
     * Returns the Track which contains the string passed as an argument
     */
    public Track getTrackByHint(String hint) {

        if(null != mTrackList && !mTrackList.isEmpty()) {

            for(Track track : mTrackList) {

                if(track.getDisplayName().toLowerCase().contains(hint)
                        || track.getArtist().toLowerCase().contains(hint)
                        || track.getAlbumName().toLowerCase().contains(hint)
                        || track.getTitle().toLowerCase().contains(hint)) {

                    return track;
                }
            }
        }

        return null;
    }


    public ArrayList<Track> getTrackList() {
        return mTrackList;
    }
}
