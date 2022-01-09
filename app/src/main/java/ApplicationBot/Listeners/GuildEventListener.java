package ApplicationBot.Listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildEventListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        System.out.println("eventjoin");
        long userId = event.getUser().getIdLong();
        long guildId = event.getGuild().getIdLong();
        JDA bot = event.getJDA();
        bot.getGuildById(guildId).addRoleToMember(userId, bot.getRoleById(804551889395646464L)).queue();
        bot.getUserById(userId).openPrivateChannel().queue(channel -> channel.sendMessage("Your ID for the application process is: " + event.getUser().getId()).queue());
    }
}
