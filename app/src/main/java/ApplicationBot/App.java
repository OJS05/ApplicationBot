package ApplicationBot;

import ApplicationBot.Listeners.ReactionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class App {
    public static JDA jda;

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault(args[0]).addEventListeners(new ReactionListener()).build();
        new Poller();
    }
}
