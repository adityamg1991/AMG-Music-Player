package muzic.coffeemug.com.muzic.Utilities;

import java.util.HashMap;

/**
 * Created by aditya on 07/09/15.
 */
public class AppConstants {

    public static final int SCROLL_THRESHOLD = 20;

    public static final String SELECTED_TRACK = "when_is_half_life_3_coming_gabe_when";

    public static final String DELETED_TRACK = "i_have_almost_given_up";

    public static final String TRACK_LIST = "track_list";


    public interface MusicPlayback {

        int TRACK_NOTI_ID = 1991;
        String NOTIFICATION_PLAY = "notification_play";
        String NOTIFICATION_PAUSE = "notification_pause";
        String NOTIFICATION_NEXT_TRACK = "notification_next_track";
        String NOTIFICATION_EXIT = "notification_exit";
        String NOTIFICATION_REMOVE_NOTI = "notification_remove_noti";
    }


    public interface SharedPref {
        int EMPTY_PROGRESS = 0;
    }

}
