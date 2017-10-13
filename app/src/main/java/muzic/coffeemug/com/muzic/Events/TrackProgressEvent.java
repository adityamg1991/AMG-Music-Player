package muzic.coffeemug.com.muzic.Events;

/**
 * Created by aditya on 25/5/16.
 */
public class TrackProgressEvent extends BaseEvent {

    private int progress;

    public TrackProgressEvent(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }
}
