import database.Database;
import database.UserFAQ;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import util.EmbedBuilders;
import util.Init;
import util.Tokens;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserCommands implements CommandExecutor {

    private static boolean isModCreator(User user) {
        return user.getRoles(Init.guild).contains(Init.user);
    }

    @Command(aliases = "setmyfaq")
    public static void setMyFAQ(ServerTextChannel c, User u, String[] args) {
        if (isModCreator(u)) {
            c.sendMessage(util.EmbedBuilders.permissionCheckFailed());
            return;
        }
        try {
            String command = args[0];
            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(args[i]);
                message.append(" ");
            }

            ArrayList<String> cmds = new ArrayList<>();

            if (Database.getUsersFAQs(u.getId()).isPresent()) {
                cmds = Database.getUsersFAQs(u.getId()).get();
            }

            if (Database.FAQExists(command)) {
                if (cmds.contains(command)) {
                    UserFAQ.setFaq(command, message.toString(), u.getId());
                    c.sendMessage(EmbedBuilders.successEmbed("Set FAQ!"));
                } else {
                    c.sendMessage(EmbedBuilders.failEmbed("Sorry, that command is already in use!"));
                }
            } else if (cmds.size() > 0) {
                UserFAQ.setFaq(command, message.toString(), u.getId());
                c.sendMessage(EmbedBuilders.successEmbed("Set FAQ!"));
            } else {
                UserFAQ.addFaq(command, message.toString(), u.getId());
                c.sendMessage(EmbedBuilders.successEmbed("Added FAQ!"));
            }
        } catch (Exception e) {
            System.out.println("Exception setting FAQ.");
            e.printStackTrace();
            c.sendMessage(EmbedBuilders.failEmbed("Could not set FAQ!"));
        }
    }

    @Command(aliases = "removemyfaq")
    public static void removeMyFAQ(ServerTextChannel c, User u) {
        if (isModCreator(u)) {
            c.sendMessage(util.EmbedBuilders.permissionCheckFailed());
            return;
        }
        try {
            UserFAQ.removeFaq(u.getId());
            c.sendMessage(EmbedBuilders.successEmbed("Removed your FAQs!"));
        } catch (SQLException e) {
            System.out.println("Exception removing FAQ.");
            c.sendMessage(EmbedBuilders.failEmbed("Could not remove your faq(s)."));
            e.printStackTrace();
        }
    }

    @Command(aliases = "list")
    public static void listFAQs(ServerTextChannel c, User u, String[] args) {
        int currPage = 0;
        if (args.length > 0) {
            currPage = Integer.valueOf(args[0]) - 1;
        }

        if (Init.registeredFAQs.size() / 5 < currPage || currPage < 0) {
            c.sendMessage(EmbedBuilders.failEmbed("Sorry, that's not a valid page.")).join();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("FAQ List : Page " + (currPage + 1))
                .setFooter("Use 'f!list <page>' to view more pages...", Tokens.ICON)
                .setColor(Tokens.EMBED_COLOR);

        for (int i = currPage; i < Init.registeredFAQs.size() && i < currPage + 5; i++) {
            embedBuilder.addField(Init.registeredFAQs.get(i)[0], Init.registeredFAQs.get(i)[1]);
        }

        c.sendMessage(embedBuilder);
    }

    @Command(aliases = "help")
    public static void help(ServerTextChannel c, User u) {
        String[][] baseCommands = new String[][]{
                {"help", "Display this help menu."},
                {"list [page]", "List all FAQs (Official and then Community, sorted alphabetically.)"}
        };
        String[][] modMakerCommands = new String[][]{
                {"setmyfaq <faq name> <faq description>", "Set your personal FAQ. **<faq name>** is one word, " +
                        "**<faq description>** can be up to 500 characters (including spaces)."},
                {"removemyfaq", "Remove your personal FAQ."}
        };
        String[][] adminCommands = new String[][]{
                {"addimagefaq <faq name> <image url>", "Adds an \"official\" embedded image FAQ. Can be a GIF or " +
                        "static image. Videos do not work here."},
                {"addfaq <faq name> <faq description>", "Adds an \"official\" FAQ."},
                {"setfaq <old faq name> <new faq name> <faq description>", "**<old faq name>** is the old 1 word faq name, " +
                        "**<new faq name>** is the new 1 word faq name (can be the old faq name), and **<faq description>** " +
                        "can be up to 500 characters (including spaces). Only works for \"official\" FAQs."},
                {"deletefaq <faq name>", "Delete **<faq name>**. Can be official or community."}
        };

        StringBuilder commandList = new StringBuilder();
        commandList.append("__General Commands__\n" +
                "**faq!<faq name>** - View **<faq name>**'s FAQ embed.\n");
        for (String[] command : baseCommands) {
            commandList.append("**"
                    .concat(Tokens.PREFIX)
                    .concat(command[0])
                    .concat("** - ")
                    .concat(command[1])
                    .concat("\n"));
        }

        if (isModCreator(u)) {
            commandList.append("------------------------------------------------------------------------------------" +
                    "\n__Personal FAQ__\n");
            for (String[] command : modMakerCommands) {
                commandList.append("**"
                        .concat(Tokens.PREFIX)
                        .concat(command[0])
                        .concat("** - ")
                        .concat(command[1])
                        .concat("\n"));
            }
        }

        if (AdminCommands.isAdmin(u)) {
            commandList.append("------------------------------------------------------------------------------------" +
                    "\n__Admin Commands__\n");
            for (String[] command : adminCommands) {
                commandList.append("**"
                        .concat(Tokens.PREFIX)
                        .concat(command[0])
                        .concat("** - ")
                        .concat(command[1])
                        .concat("\n"));
            }
        }
        c.sendMessage(new EmbedBuilder()
                .setTitle("Help")
                .setDescription(commandList.toString())
                .setColor(Tokens.EMBED_COLOR)
                .setFooter("FAQBot", Tokens.ICON));
    }
}
