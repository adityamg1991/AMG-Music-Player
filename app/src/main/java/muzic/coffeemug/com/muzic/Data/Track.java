package muzic.coffeemug.com.muzic.Data;

import android.os.Parcel;
import android.os.Parcelable;

import muzic.coffeemug.com.muzic.Streaming.Models.SoundCloudTrack;

/**
 * Created by aditya on 01/09/15.
 */
public class Track implements Parcelable {

    private String ID;
    private String ARTIST;
    // The name of the song
    private String TITLE;
    // Contains the file path
    private String DATA;
    private String DISPLAY_NAME;
    // The duration of the audio file, in ms
    private long DURATION;
    private String ALBUM_NAME;
    // Use this to get Album Art
    private String ALBUM_ID;
    // Field only contains value when retrieving from database
    private int TIMES_PLAYED = -1;
    // Last played time in unix timestamp
    private long LAST_PLAYED = -1;

    public SoundCloudTrack getSoundCloudTrack() {
        return soundCloudTrack;
    }

    public void setSoundCloudTrack(SoundCloudTrack soundCloudTrack) {
        this.soundCloudTrack = soundCloudTrack;
    }

    // In all other cases except SoundCloud Tracks, this will be null;
    private SoundCloudTrack soundCloudTrack;

    // Constructor used when querying data from android database
    public Track(String ID, String ARTIST, String TITLE,
                 String DATA, String DISPLAY_NAME, long DURATION, String ALBUM_NAME, String ALBUM_ID) {
        this.ID = ID;
        this.ARTIST = ARTIST;
        this.TITLE = TITLE;
        this.DATA = DATA;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.DURATION = DURATION;
        this.ALBUM_NAME = ALBUM_NAME;
        this.ALBUM_ID = ALBUM_ID;
    }

    // Constructor used when querying data from android database
    public Track(String ID, String ARTIST, String TITLE,
                 String DATA, String DISPLAY_NAME, long DURATION,
                 String ALBUM_NAME, String ALBUM_ID, int TIMES_PLAYED, long LAST_PLAYED) {
        this.ID = ID;
        this.ARTIST = ARTIST;
        this.TITLE = TITLE;
        this.DATA = DATA;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.DURATION = DURATION;
        this.ALBUM_NAME = ALBUM_NAME;
        this.ALBUM_ID = ALBUM_ID;
        this.TIMES_PLAYED = TIMES_PLAYED;
        this.LAST_PLAYED = LAST_PLAYED;
    }


    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getID() {
        return ID;
    }

    public String getArtist() {
        return ARTIST;
    }

    public String getTitle() {
        return TITLE;
    }

    public String getData() {
        return DATA;
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public long getDuration() {
        return DURATION;
    }

    public String getAlbumName() {
        return ALBUM_NAME;
    }


    public String toString() {
        return ID + " | " + ARTIST + " | " + TITLE + " | " +
                DATA + " | " + DISPLAY_NAME + " | " + DURATION;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public Track(Parcel parcel) {

        ID = parcel.readString();
        ARTIST = parcel.readString();
        TITLE = parcel.readString();
        DATA = parcel.readString();
        DISPLAY_NAME = parcel.readString();
        DURATION = parcel.readLong();
        ALBUM_NAME = parcel.readString();
        ALBUM_ID = parcel.readString();
    }


    public String getAlbumID() {
        return ALBUM_ID;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(ID);
        dest.writeString(ARTIST);
        dest.writeString(TITLE);
        dest.writeString(DATA);
        dest.writeString(DISPLAY_NAME);
        dest.writeLong(DURATION);
        dest.writeString(ALBUM_NAME);
        dest.writeString(ALBUM_ID);
    }


    public int getTimesPlayed() {
        return TIMES_PLAYED;
    }


    public void setTimesPlayed(int TIMES_PLAYED) {
        this.TIMES_PLAYED = TIMES_PLAYED;
    }


    public long getLastPlayed() {
        return LAST_PLAYED;
    }


    public void setLastPlayed(long LAST_PLAYED) {
        this.LAST_PLAYED = LAST_PLAYED;
    }


}
