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
import java.util.Random;

import muzic.coffeemug.com.muzic.MusicPlayback.PlaybackController;
import muzic.coffeemug.com.muzic.Utilities.AppConstants;
import muzic.coffeemug.com.muzic.Data.Track;
import muzic.coffeemug.com.muzic.Utilities.SharedPrefs;

/**
 * Created by aditya on 11/09/15.
 */
public class TrackStore {

    private Context context;
    private static TrackStore instance;
    private ArrayList<Track> mTrackList;
    private ResultReceiver resultReceiver;
    private Random random;


    public TrackStore(Context context) {
        this.context = context;
        random = new Random();
    }


    public static TrackStore getInstance(Context context) {

        if(null == instance) {
            instance = new TrackStore(context);
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
                    long duration = cursor.getLong(5);
                    String albumName = cursor.getString(6);
                    String albumID = cursor.getString(7);

                    Track track = new Track(id, artist, title, data, name, duration, albumName, albumID);
                    mTrackList.add(track);
                }

                cursor.close();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(AppConstants.TRACK_LIST, mTrackList);
                resultReceiver.send(Activity.RESULT_OK, bundle);

                return null;
            }
        }.execute();
    }


    /**
     * Returns the Track which contains the string passed as an argument
     */
    public Track getTrackByHint(String hint) {

        if(null != mTrackList && !mTrackList.isEmpty() && null != hint) {

            hint = hint.toLowerCase();
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

    /**
     * Returns the first track in the list. Might also return NULL if no tracks are present
     * @return
     */
    public Track getFirstTrack() {

        Track track = null;

        if(null != mTrackList && !mTrackList.isEmpty()) {
            track = mTrackList.get(0);
        }

        return track;
    }


    public ArrayList<Track> getTrackList() {
        return mTrackList;
    }


    public void saveInPrefsAndPlayTrack(Track track) {
        SharedPrefs.getInstance(context).storeTrack(track);
        PlaybackController.getInstance(context).playTrack();
    }


    public Track getNextLinearTrack() {

        Track track = SharedPrefs.getInstance(context).getStoredTrack();

        if (null == track) {
            return null;
        }

        int position = -1;

        for (int i=0; i<mTrackList.size(); i++) {
            Track item = mTrackList.get(i);
            if (item.getID().equals(track.getID())) {
                position = i;
                break;
            }
        }

        position++;
        if (position == mTrackList.size()) {
            position = 0;
        }

        return mTrackList.get(position);
    }


    public Track getNextRandomTrack() {
        return  mTrackList.get(random.nextInt(mTrackList.size()));
    }

    public Track getPreviousLinearTrack() {

        Track track = SharedPrefs.getInstance(context).getStoredTrack();

        if (null == track) {
            return null;
        }

        int position = -1;

        for (int i=0; i<mTrackList.size(); i++) {
            Track item = mTrackList.get(i);
            if (item.getID().equals(track.getID())) {
                position = i;
                break;
            }
        }

        position--;
        if (position == -1) {
            position = mTrackList.size()-1;
        }

        if (-2 != position) {
            return mTrackList.get(position);
        } else {
            return null;
        }

    }


    public Track getTrackByMediaID(String id) {

        for (Track track : mTrackList) {
            if (track.getID().equals(id)) {
                return track;
            }
        }
        return null;
    }

}
