import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import org.javacord.api.entity.activity.ActivityType;
import util.Init;
import util.Tokens;

import java.io.File;
import java.sql.SQLException;

class Main {
    public static void main(String[] args) {
        try {
            if (!new File("database.mv.db").exists()) {
                System.out.println("Database file not found, making a new one.");
                try {
                    database.Database.resetTables();
                } catch (SQLException e) {
                    System.out.println("Could not create DB file.");
                    throw new SQLException();
                }
            }
            Init.init(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        Init.api.updateActivity(ActivityType.WATCHING, "you code");
        Init.api.getServerTextChannels().forEach(c -> c.sendMessage("Hello, world!"));

        FAQListener.addFAQListener();

        CommandHandler handler = new JavacordHandler(Init.api);
        handler.setDefaultPrefix(Tokens.COMMAND_PREFIX);
        handler.registerCommand(new UserCommands());
        handler.registerCommand(new AdminCommands());
    }
}
