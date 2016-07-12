package muzic.coffeemug.com.muzic.Streaming.Store;
import java.util.ArrayList;

import muzic.coffeemug.com.muzic.Streaming.Models.SoundCloudTrack;

/**
 * Created by PAVILION on 6/12/2016.
 */
public class StreamTrackStore {

    private static StreamTrackStore instance;
    private ArrayList<SoundCloudTrack> dataSet;


    private StreamTrackStore() {

    }


    public static StreamTrackStore getInstance() {
        if (null == instance) {
            instance = new StreamTrackStore();
        }
        return instance;
    }


    public void setDataSet(SoundCloudTrack dataSet[]) {
        ArrayList<SoundCloudTrack> list = new ArrayList<>();
        for (int i=0; i<dataSet.length; i++) {
            list.add(dataSet[i]);
        }
        this.dataSet = list;
    }


    public ArrayList<SoundCloudTrack> getDataSet() {
        return this.dataSet;
    }


    public void setDataSet(ArrayList<SoundCloudTrack> currentlyPlayingList) {
        this.dataSet.clear();
        this.dataSet = currentlyPlayingList;
    }
}
