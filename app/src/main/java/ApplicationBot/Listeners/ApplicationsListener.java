package ApplicationBot.Listeners;

import ApplicationBot.ConfigManager;

import java.util.Timer;
import java.util.TimerTask;

public class ApplicationsListener {
    public ApplicationsListener() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {



            }
        }, 0L, ConfigManager.POLLING_INTERVAL);
    }
}
