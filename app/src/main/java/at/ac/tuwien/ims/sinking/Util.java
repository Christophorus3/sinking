package at.ac.tuwien.ims.sinking;

import java.util.concurrent.TimeUnit;

public class Util {

    public static String formatTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }
}
