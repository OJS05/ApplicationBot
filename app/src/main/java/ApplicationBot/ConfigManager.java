package ApplicationBot;

import de.leonhard.storage.Config;

import java.io.File;

public class ConfigManager {

    public static final int POLLING_INTERVAL = 60;

    public static final String USERNAME = "applications";
    public static final String PASSWORD = "B7s4T&dK";

    public static final String APPLICATION_URL = "jdbc:postgresql://postgres:5432/applications";

    public static final String WHITELIST_URL = "jdbc:postgresql://postgres:5432/whitelist";

    public static final String ARCHIVE_URL = "jdbc:postgresql://postgres:5432/archive";

    public static final Long CHANNEL_ID = 825281666636316672L;
    public static final String[] ADMIN_IDS = {};
}

