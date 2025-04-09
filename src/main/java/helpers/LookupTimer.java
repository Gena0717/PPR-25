package helpers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * look up and download new speeches every 15 minutes.
 *
 * @author Oliwia Daszczynska
 */
public class LookupTimer {
    /**
     * starts downloading protocols after 15 minutes delay. then downloads protocols every 15 minutes.
     *
     * @author Oliwia Daszczynska
     */
    public static void startLookupTimer() {
        int fifteenMinutes = (int) 9e5; // 9 x 10^5
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Downloader.DownloadProtocols();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task, fifteenMinutes, fifteenMinutes);
    }
}
