package muzic.coffeemug.com.muzic.Data;

/**
 * Created by aditya on 01/09/15.
 */
public class Track {

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
}
