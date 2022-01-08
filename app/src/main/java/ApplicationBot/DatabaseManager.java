package ApplicationBot;

import com.sun.tools.javac.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;
import pro.husk.db.GenericDataSource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static GenericDataSource whitelistSQL;

    private static GenericDataSource applicationSQL;

    private static GenericDataSource archiveSQL;

    public static GenericDataSource getOrCreateWhitelistConnection() {
        if (whitelistSQL == null) {
            whitelistSQL = new GenericDataSource(ConfigManager.WHITELIST_URL, ConfigManager.USERNAME, ConfigManager.PASSWORD);
        }
        return whitelistSQL;
    }

    public static GenericDataSource getOrCreateApplicationConnection() {
        if (applicationSQL == null) {
            applicationSQL = new GenericDataSource(ConfigManager.APPLICATION_URL, ConfigManager.USERNAME, ConfigManager.PASSWORD);
        }
        return applicationSQL;
    }

    public static GenericDataSource getOrCreateArchiveConnection() {
        if (archiveSQL == null) {
            archiveSQL = new GenericDataSource(ConfigManager.ARCHIVE_URL, ConfigManager.USERNAME, ConfigManager.PASSWORD);
        }
        return archiveSQL;
    }

    public static void checkApplicationsForNew() throws SQLException {
        ResultSet resultSet = getOrCreateApplicationConnection().query("SELECT * FROM applications WHERE checked = 'false'");
        while (resultSet.next()) {
            String uuid = getUuid(resultSet.getString("username"));

            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("New Application")
                    .setThumbnail("https://mc-heads.net/avatar/" + uuid + "/100.png")
                    .setFooter(String.valueOf(resultSet.getInt("id")))
                    .addField("Username", resultSet.getString("username"), false)
                    .addField("Age", resultSet.getString("age"), false)
                    .addField("Why do you want to play on Purple?", resultSet.getString("reason"), false);

            getOrCreateApplicationConnection().updateAsync("UPDATE applications SET checked = '1' WHERE id = '" + resultSet.getString("id") + "'");
            App.jda.getTextChannelById(ConfigManager.CHANNEL_ID.toString()).sendMessageEmbeds(eb.build()).queue(message -> {
                message.addReaction("U+2705").queue();
                message.addReaction("U+274C").queue();
            });

        }
    }

    public static String getUuid(String name) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            return json.getString("id");
        } catch (Exception e) {
            return e.toString();
        }
    }

    public static int getVotesFor(int id) throws SQLException {
        int votesFor = 0;
        ResultSet resultSet = getOrCreateApplicationConnection().query("SELECT votes_for FROM applications WHERE id =" + id);
        while (resultSet.next()) {
            if(resultSet.getArray("votes_for") != null) {
                votesFor = ((Long[]) resultSet.getArray("votes_for").getArray()).length;
            }else{
                votesFor = 0;
            }
        }
        resultSet.close();
        return votesFor;
    }

    public static void addVoteFor(int id, String voterId) throws SQLException {
        getOrCreateApplicationConnection().update("UPDATE applications SET votes_for = array_append(votes_for," + voterId + ") WHERE id = '" + id + "'");
    }

    public static void addVoteAgainst(int id, String voterId) throws SQLException {
        getOrCreateApplicationConnection().update("UPDATE applications SET votes_against = array_append(votes_against," + voterId + ") WHERE id = '" + id + "'");
    }

    public static void removeVoteFor(int id, String voterId) throws SQLException {
        getOrCreateApplicationConnection().update("UPDATE applications SET votes_for = array_remove(votes_for," + voterId + ") WHERE id = '" + id + "'");
    }

    public static void removeVoteAgainst(int id, String voterId) throws SQLException {
        getOrCreateApplicationConnection().update("UPDATE applications SET votes_against = array_remove(votes_against," + voterId + ") WHERE id = '" + id + "'");
    }

    public static int getVotesAgainst(int id) throws SQLException {
        int votesAgainst = 0;
        ResultSet resultSet = getOrCreateApplicationConnection().query("SELECT votes_against FROM applications WHERE id =" + id);
        while (resultSet.next()) {
            if(resultSet.getArray("votes_against") != null) {
                votesAgainst = ((Long[]) resultSet.getArray("votes_against").getArray()).length;
            }else{
                votesAgainst = 0;
            }
        }
        resultSet.close();
        return votesAgainst;
    }

    public static void acceptApplication(int id) throws SQLException {
        ResultSet resultSet = getOrCreateApplicationConnection().query("SELECT * FROM applications WHERE id =" + id);
        String username = "";
        int age = 0;
        String reason = "";
        while (resultSet.next()) {
            username = resultSet.getString("username");
            age = resultSet.getInt("age");
            reason = resultSet.getString("reason");
        }
        resultSet.close();

        getOrCreateApplicationConnection().update("DELETE FROM applications WHERE id = '" + id + "'");
        getOrCreateArchiveConnection().update("INSERT INTO archive(username, age, reason, status) VALUES ('" + username + "','" + age + "','" + reason + "','accepted')");
        getOrCreateWhitelistConnection().update("INSERT INTO whitelist (username, uuid) VALUES ('" + username + "', '" + getUuid(username) + "')");

    }

    public static void rejectApplication(int id) throws SQLException {
        ResultSet resultSet = getOrCreateApplicationConnection().query("SELECT * FROM applications WHERE id =" + id);
        String username = "";
        int age = 0;
        String reason = "";
        while (resultSet.next()) {
            username = resultSet.getString("username");
            age = resultSet.getInt("age");
            reason = resultSet.getString("reason");
        }
        resultSet.close();

        getOrCreateApplicationConnection().update("DELETE FROM applications WHERE id = '" + id + "'");
        getOrCreateArchiveConnection().update("INSERT INTO archive(username, age, reason, status) VALUES ('" + username + "','" + age + "','" + reason + "','rejected')");
    }

    public static boolean hasVoted(int id, String voterId) throws SQLException {
        ResultSet resultSet = getOrCreateApplicationConnection().query("SELECT * FROM applications WHERE id =" + id);
        boolean hasVoted = false;

        while (resultSet.next()) {
            if(resultSet.getArray("votes_for") == null || resultSet.getArray("votes_against") == null) return false;

            for (long voter : (Long[]) resultSet.getArray("votes_for").getArray()) {
                if(voter == Long.parseLong(voterId)) {
                    hasVoted = true;
                }
            }
            for (long voter : (Long[]) resultSet.getArray("votes_against").getArray()) {
                if(voter == Long.parseLong(voterId)) {
                    hasVoted = true;
                }
            }
        }

        resultSet.close();
        return hasVoted;
    }
}

