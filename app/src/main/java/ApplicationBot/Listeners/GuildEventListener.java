package ApplicationBot.Listeners;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildEventListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        System.out.println("eventjoin");
        Member user = event.getMember();
        long guildId = event.getGuild().getIdLong();
        JDA bot = event.getJDA();
        bot.getGuildById(guildId).addRoleToMember(user.getId(), bot.getRoleById(804551889395646464L)).queue();
        user.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("Your ID for the application process is: " + event.getUser().getId()).queue());
    }
}
