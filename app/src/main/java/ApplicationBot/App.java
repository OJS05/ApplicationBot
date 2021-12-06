package ApplicationBot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Channel;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;

public class App {

    public static JDA jda;

    public static void main(String[] args) throws LoginException, URISyntaxException, IOException, InterruptedException {
        //jda = JDABuilder.createDefault(ConfigManager.TOKEN).build();
        System.out.println(DatabaseManager.getUuid("ojs05"));
    }
}
