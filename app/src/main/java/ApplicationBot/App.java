package ApplicationBot;

import ApplicationBot.Listeners.GuildEventListener;
import ApplicationBot.Listeners.ReactionListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class App {
    public static JDA jda;

    public static void main(String[] args) throws LoginException {
        System.out.println(args[0]);
        jda = JDABuilder.createDefault(args[0]).enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS).build();
        jda.addEventListener(new GuildEventListener());
        jda.addEventListener(new ReactionListener());
        System.out.println("debug");
        new Poller();
    }
}
