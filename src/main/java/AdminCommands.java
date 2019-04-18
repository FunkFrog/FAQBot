import database.Database;
import database.SysFAQ;
import database.UserFAQ;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.user.User;
import util.EmbedBuilders;
import util.Init;

import java.sql.SQLException;

public class AdminCommands implements CommandExecutor {
    static boolean isAdmin(User user) {
        return user.getRoles(Init.guild).contains(Init.admin)
            || Init.authorizedUsers.contains(user.getId());
    }

    @Command(aliases = "addimagefaq")
    public static void addImageFAQ(ServerTextChannel c, User u, String[] args) {
        if (!isAdmin(u)) {
            c.sendMessage(util.EmbedBuilders.permissionCheckFailed());
            return;
        }
        try {
            String command = args[0];
            String image = args[1];
            if (!Database.FAQExists(command)) {
                SysFAQ.addSystemImageFaq(command, image);
                c.sendMessage(EmbedBuilders.successEmbed("Added image embed!"));
            } else {
                c.sendMessage(EmbedBuilders.failEmbed("Command already exists!"));
            }
        } catch (SQLException e) {
            System.out.println("Exception adding image FAQ.");
            c.sendMessage(EmbedBuilders.failEmbed("An internal error has occurred."));
            e.printStackTrace();
        }
    }

    @Command(aliases = "addfaq")
    public static void addFAQ(ServerTextChannel c, User u, String[] args) {
        if (!isAdmin(u)) {
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
            if (!Database.FAQExists(command)) {
                SysFAQ.addFaq(command, message.toString());
                c.sendMessage(EmbedBuilders.successEmbed("Added FAQ!"));
            } else {
                c.sendMessage(EmbedBuilders.failEmbed("Command already exists!"));
            }
        } catch (SQLException e) {
            System.out.println("Exception adding sys FAQ.");
            c.sendMessage(EmbedBuilders.failEmbed("An internal error has occurred."));
            e.printStackTrace();
        }
    }

    @Command(aliases = "setfaq")
    public static void setFAQ(ServerTextChannel c, User u, String[] args) {
        if (!isAdmin(u)) {
            c.sendMessage(util.EmbedBuilders.permissionCheckFailed());
            return;
        }
        try {
            String oldCommand = args[0];
            String command = args[1];
            StringBuilder message = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                message.append(args[i]);
                message.append(" ");
            }
            if (SysFAQ.getSysFAQMessage(command).isPresent()) {
                SysFAQ.setFaq(command, message.toString(), oldCommand);
                c.sendMessage(EmbedBuilders.successEmbed("Set FAQ!"));
            } else if (Database.FAQExists(command)) {
                c.sendMessage(EmbedBuilders.failEmbed("FAQ is not a system FAQ!"));
            } else {
                c.sendMessage(EmbedBuilders.failEmbed("FAQ doesn't exist!"));
            }
        } catch (SQLException e) {
            System.out.println("Exception setting sys FAQ.");
            c.sendMessage(EmbedBuilders.failEmbed("An internal error has occurred."));
            e.printStackTrace();
        }
    }

    @Command(aliases = "deletefaq")
    public static void deleteFAQ(ServerTextChannel c, User u, String[] args) {
        if (!isAdmin(u)) {
            c.sendMessage(util.EmbedBuilders.permissionCheckFailed());
            return;
        }
        try {
            String command = args[0];
            if (SysFAQ.getSysFAQMessage(command).isPresent()) {
                SysFAQ.removeFaq(command);
                c.sendMessage(EmbedBuilders.successEmbed("Removed system FAQ!"));
            } else if (UserFAQ.getFAQOwner(command).isPresent()) {
                UserFAQ.removeFaq(UserFAQ.getFAQOwner(command).get());
                c.sendMessage(EmbedBuilders.successEmbed("Removed user's FAQ(s)!"));
            } else {
                c.sendMessage(EmbedBuilders.failEmbed("FAQ does not exist!"));
            }
        } catch (SQLException e) {
            System.out.println("Exception deleting FAQ.");
            c.sendMessage(EmbedBuilders.failEmbed("An internal error has occurred."));
            e.printStackTrace();
        }
    }
}
