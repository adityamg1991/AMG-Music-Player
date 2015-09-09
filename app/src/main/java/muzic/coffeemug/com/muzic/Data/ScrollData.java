package muzic.coffeemug.com.muzic.Data;

/**
 * Created by aditya on 07/09/15.
 */
public class ScrollData {

    private static ScrollData instance;

    private boolean isBottomBarVisible = true;
    private int scrolledDistance = 0;

    private ScrollData(){};

    public static ScrollData getInstance() {

        if(null == instance) {
            instance = new ScrollData();
        }
        return instance;
    }


    public boolean isBottomBarVisible() {
        return isBottomBarVisible;
    }


    public void setData(boolean isBottomBarVisible, int scrolledDistance) {
        this.isBottomBarVisible = isBottomBarVisible;
        this.scrolledDistance = scrolledDistance;
    }


    public void reset() {
        isBottomBarVisible = true;
        scrolledDistance = 0;
    }


    public int getScrolledDistance() {
        return scrolledDistance;
    }
}
