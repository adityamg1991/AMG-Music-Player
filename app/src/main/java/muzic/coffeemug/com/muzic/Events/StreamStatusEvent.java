package muzic.coffeemug.com.muzic.Events;

/**
 * Created by PAVILION on 6/15/2016.
 */
public class StreamStatusEvent extends BaseEvent {

    private boolean playing;

    public StreamStatusEvent(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return playing;
    }

}
