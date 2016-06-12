package muzic.coffeemug.com.muzic.Utilities;

import java.util.HashMap;

/**
 * Created by aditya on 07/09/15.
 */
public interface AppConstants {

    int SCROLL_THRESHOLD = 20;

    String SELECTED_TRACK = "when_is_half_life_3_coming_gabe_when";

    String DELETED_TRACK = "i_have_almost_given_up";

    String TRACK_LIST = "track_list";


    interface MusicPlayback {

        int TRACK_NOTI_ID = 1991;
        String NOTIFICATION_PLAY = "notification_play";
        String NOTIFICATION_PAUSE = "notification_pause";
        String NOTIFICATION_NEXT_TRACK = "notification_next_track";
        String NOTIFICATION_EXIT = "notification_exit";
        String NOTIFICATION_REMOVE_NOTI = "notification_remove_noti";
    }


    interface SharedPref {
        int EMPTY_PROGRESS = 0;
    }


    interface SoundCloud {

        String CLIENT_ID = "d734fcc82b1f04127dd928093ef9c5f3";
        String BASE_URL = "https://api.soundcloud.com";
        String CLIENT_ID_INFO = "client_id=" + CLIENT_ID;

        // Requires 'client_id' and 'q'
        String SEARCH_URL = BASE_URL + "/tracks?" + CLIENT_ID_INFO;

        String STREAM_URL_FOOTER = "/stream?" + CLIENT_ID_INFO;
        String STREAM_URL_HEADER = BASE_URL + "/tracks/";

    }

}

