package ApplicationBot;

import de.leonhard.storage.Config;

import java.io.File;

public class ConfigManager {

    public static final String TOKEN = getOrCreateConfig().getString("botToken");
    public static final Long CHANNEL_ID = getOrCreateConfig().getLong("channelID");
    public static final char PREFIX = getOrCreateConfig().getString("prefix").charAt(0);
    public static final String OWNER_ID = getOrCreateConfig().getString("ownerID");
    public static final String[] ADMIN_IDS = getOrCreateConfig().getString("adminIDs").split(",");

    public static final int POLLING_INTERVAL = getOrCreateConfig().getInt("pollingInterval");
    public static final String APPLICATION_DATABASE = getOrCreateConfig().getString("applicationDatabase");
    public static final String APPLICATION_USERNAME = getOrCreateConfig().getString("applicationUsername");
    public static final String APPLICATION_PASSWORD = getOrCreateConfig().getString("applicationPassword");
    public static final String APPLICATION_HOST = getOrCreateConfig().getString("applicationHost");
    public static final String APPLICATION_PORT = getOrCreateConfig().getString("applicationPort");
    public static final String APPLICATION_URL = "jdbc:mysql://" + APPLICATION_HOST + ":" + APPLICATION_PORT + "/" + APPLICATION_DATABASE;

    public static final String WHITELIST_DATABASE = getOrCreateConfig().getString("whitelistDatabase");
    public static final String WHITELIST_USERNAME = getOrCreateConfig().getString("whitelistUsername");
    public static final String WHITELIST_PASSWORD = getOrCreateConfig().getString("whitelistPassword");
    public static final String WHITELIST_HOST = getOrCreateConfig().getString("whitelistHost");
    public static final String WHITELIST_PORT = getOrCreateConfig().getString("whitelistPort");
    public static final String WHITELIST_URL = "jdbc:mysql://" + WHITELIST_HOST + ":" + WHITELIST_PORT + "/" + WHITELIST_DATABASE;

    public static Config getOrCreateConfig(){

        if(!new File("config.json").exists()){
            Config config = new Config(new File("config.toml"));
            config.setDefault("botToken", "token");
            config.setDefault("channelID", "id");
            config.setDefault("prefix", "!");
            config.setDefault("owner", "id");
            config.setDefault("adminIds", new String[]{"1", "2", "3"});

            config.setDefault("pollingInterval", 60);
            config.setDefault("applicationDatabase", "database");
            config.setDefault("applicationUsername", "username");
            config.setDefault("applicationPassword", "password");
            config.setDefault("applicationHostname", "hostname");
            config.setDefault("applicationPort", "port");

            config.setDefault("whitelistDatabase", "database");
            config.setDefault("whitelistUsername", "username");
            config.setDefault("whitelistPassword", "password");
            config.setDefault("whitelistHostname", "hostname");
            config.setDefault("whitelistPort", "port");

            return config;
        }else{
            Config config = new Config(new File("config.toml"));
            return config;
        }
    }

}
