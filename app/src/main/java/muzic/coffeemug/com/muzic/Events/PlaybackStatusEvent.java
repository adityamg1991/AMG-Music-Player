package muzic.coffeemug.com.muzic.Events;

/**
 * Created by aditya on 1/6/16.
 */
public class PlaybackStatusEvent extends BaseEvent {

    private boolean playing;

    public PlaybackStatusEvent(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return playing;
    }
}
