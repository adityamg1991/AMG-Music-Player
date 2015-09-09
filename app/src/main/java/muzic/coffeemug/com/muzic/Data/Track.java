package muzic.coffeemug.com.muzic.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aditya on 01/09/15.
 */
public class Track implements Parcelable {

    private String ID;
    private String ARTIST;
    private String TITLE;
    private String DATA;
    private String DISPLAY_NAME;
    private String DURATION;
    private String ALBUM_NAME;


    public Track(String ID, String ARTIST, String TITLE,
                 String DATA, String DISPLAY_NAME, String DURATION, String ALBUM_NAME) {
        this.ID = ID;
        this.ARTIST = ARTIST;
        this.TITLE = TITLE;
        this.DATA = DATA;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.DURATION = DURATION;
        this.ALBUM_NAME = ALBUM_NAME;

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

    public String getDuration() {
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
        DURATION = parcel.readString();
        ALBUM_NAME = parcel.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(ID);
        dest.writeString(ARTIST);
        dest.writeString(TITLE);
        dest.writeString(DATA);
        dest.writeString(DISPLAY_NAME);
        dest.writeString(DURATION);
        dest.writeString(ALBUM_NAME);
    }
}
