import database.Database;
import database.SysFAQ;
import database.UserFAQ;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import util.EmbedBuilders;
import util.Init;
import util.Tokens;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

public class Main {
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

        Init.api.addMessageCreateListener(message -> {
            try {
                if (message.getReadableMessageContent().isEmpty()
                        || message.getReadableMessageContent().length() < 5) return;
                String msg = message.getReadableMessageContent();
                if (msg.substring(0, 4).equalsIgnoreCase("FAQ!")) {
                    String command = msg.substring(4).toLowerCase();
                    Optional<String> faq = UserFAQ.getFAQMessage(command);
                    Optional<String> sysfaq = SysFAQ.getSysFAQMessage(command);
                    if (faq.isPresent()) {
                        message.getServerTextChannel().ifPresent(ch -> ch.sendMessage(new EmbedBuilder()
                                .setTitle("FAQ: " + msg.substring(4).toUpperCase())
                                .setDescription(faq.get())
                                .setFooter("FAQBot", Tokens.ICON)
                                .setColor(Tokens.EMBED_COLOR)));
                    } else if (sysfaq.isPresent()) {
                        if (SysFAQ.isImageEmbed(command)) {
                            Optional<String> image = SysFAQ.getFAQImage(command);
                            image.ifPresent(s -> message.getServerTextChannel()
                                    .ifPresent(ch -> ch.sendMessage(new EmbedBuilder()
                                            .setTitle("FAQ: " + msg.substring(4).toUpperCase())
                                            .setImage(s)
                                            .setFooter("FAQBot", Tokens.ICON)
                                            .setColor(Tokens.EMBED_COLOR))));
                        } else {
                            message.getServerTextChannel().ifPresent(ch -> ch.sendMessage(new EmbedBuilder()
                                    .setTitle("FAQ: " + msg.substring(4).toUpperCase())
                                    .setDescription(sysfaq.get())
                                    .setFooter("FAQBot", Tokens.ICON)
                                    .setColor(Tokens.EMBED_COLOR)));
                        }
                    } else {
                        message.getServerTextChannel().ifPresent(ch -> ch.sendMessage(
                                EmbedBuilders.failEmbed("Could not find that FAQ!")));
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception handling message.");
                e.printStackTrace();
            }
        });

        CommandHandler handler = new JavacordHandler(Init.api);
        handler.setDefaultPrefix(Tokens.PREFIX);
        handler.registerCommand(new UserCommands());
        handler.registerCommand(new AdminCommands());
    }
}
