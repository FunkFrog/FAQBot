package util;

import database.Database;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;

import java.util.ArrayList;
import java.util.Arrays;

public class Init {
    public static DiscordApi api;
    public static Role admin, user;
    public static ArrayList<Long> authorizedUsers;
    public static Server guild;
    public static ArrayList<String[]> registeredFAQs;

    public static void init(String DISCORD_TOKEN) {
        api = new DiscordApiBuilder().setToken(DISCORD_TOKEN).login().join();

        admin = api.getRoleById(Tokens.ADMIN_ROLE).orElseThrow(NullPointerException::new);
        user = api.getRoleById(Tokens.USER_ROLE).orElseThrow(NullPointerException::new);

        guild = api.getServerById(Tokens.GUILD).orElseThrow(NullPointerException::new);

        authorizedUsers = new ArrayList<>();
        authorizedUsers.addAll(Arrays.asList(Tokens.AUTHORIZED_USERS));

        refreshFAQs();
    }

    public static void refreshFAQs() {
        registeredFAQs = Database.getFAQs();
    }
}