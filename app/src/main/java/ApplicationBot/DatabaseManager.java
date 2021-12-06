package ApplicationBot;

import com.mysql.cj.result.Row;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.JSONObject;
import pro.husk.mysql.MySQL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static MySQL whitelistSQL;

    private static MySQL applicationSQL;

    public static MySQL getOrCreateWhitelistConnection(){
        if(whitelistSQL == null){
            whitelistSQL = new MySQL(ConfigManager.WHITELIST_URL, ConfigManager.WHITELIST_USERNAME, ConfigManager.WHITELIST_PASSWORD);
        }
        return whitelistSQL;
    }

    public static MySQL getOrCreateApplicationConnection(){
        if(applicationSQL == null){
            applicationSQL = new MySQL(ConfigManager.APPLICATION_URL, ConfigManager.APPLICATION_USERNAME, ConfigManager.APPLICATION_PASSWORD);
        }
        return applicationSQL;
    }

    public static void checkApplicationsForNew() throws SQLException {
        ResultSet resultSet = getOrCreateApplicationConnection().query("SELECT * FROM applications WHERE 'status' = 'new'");
        while(resultSet.next()){
            String uuid = getUuid(resultSet.getString("username"));

            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("New Application")
                    .setThumbnail("https://mc-heads.net/avatar/" + uuid + "/100.png")
                    .addField("Username", resultSet.getString("username"), false)
                    .addField("Age", resultSet.getString("age"), false)
                    .addField("Why do you want to play on Purple?", resultSet.getString("reason"), false);


            App.jda.getTextChannelById("");
        }
    }

    public static String getUuid(String name){
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            return json.getString("id");
        }catch (Exception e){
            return e.toString();
        }
    }
}
