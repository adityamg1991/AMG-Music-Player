package muzic.coffeemug.com.muzic.Utilities;

import java.util.HashMap;

import muzic.coffeemug.com.muzic.R;

/**
 * Created by aditya on 30/5/16.
 */
public class PlayStyle {


    public static final int REPEAT_ALL  = 0;
    public static final int REPEAT_ONE  = 1;
    public static final int SHUFFLE     = 2;

    private static HashMap<Integer, Integer> map = new HashMap<>();


    static {
        if (map.isEmpty()) {
            map.put(REPEAT_ALL, R.drawable.play_style_repeat_all_white);
            map.put(REPEAT_ONE, R.drawable.play_style_repeat_white);
            map.put(SHUFFLE, R.drawable.play_style_shuffle_white);
        }
    }

    public static int getPlayStyleDrawable(int i) {
        return map.get(i);
    }

}
