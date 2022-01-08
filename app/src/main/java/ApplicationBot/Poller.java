package ApplicationBot;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Poller {
    public Poller() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    DatabaseManager.checkApplicationsForNew();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0L, ConfigManager.POLLING_INTERVAL);
    }
}
