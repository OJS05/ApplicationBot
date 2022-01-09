package ApplicationBot.Listeners;

import ApplicationBot.DatabaseManager;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;

public class ReactionListener extends ListenerAdapter {
    private static int id = 0;

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getChannel().getId().equals("929570131980460042")){
            event.retrieveMessage().queue(message -> {
                id = Integer.parseInt(message.getEmbeds().get(0).getFooter().getText());

                if (event.getReactionEmote().getAsCodepoints().equals("U+2705")) {
                    try {
                        if (event.getUser().isBot()) return;
                        if (!DatabaseManager.hasVoted(id, event.getUser().getId())) {
                            DatabaseManager.addVoteFor(id, event.getUser().getId());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (DatabaseManager.getVotesFor(id) >= 3) {
                            DatabaseManager.acceptApplication(id);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (event.getReactionEmote().getAsCodepoints().equals("U+274c")) {
                    try {
                        if(event.getUser().isBot()) return;
                        if (!DatabaseManager.hasVoted(id, event.getUser().getId())) {
                            DatabaseManager.addVoteAgainst(id, event.getUser().getId());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (DatabaseManager.getVotesAgainst(id) >= 3) {
                            DatabaseManager.rejectApplication(id);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if(event.getChannel().getId().equals("929570131980460042")){
            event.retrieveMessage().queue(message -> {
                id = Integer.parseInt(message.getEmbeds().get(0).getFooter().getText());

                if (event.getReactionEmote().getAsCodepoints().equals("U+2705")) {
                    try {
                        DatabaseManager.removeVoteFor(id, event.getUser().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (event.getReactionEmote().getAsCodepoints().equals("U+274c")) {
                    try {
                        DatabaseManager.removeVoteAgainst(id, event.getUser().getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

