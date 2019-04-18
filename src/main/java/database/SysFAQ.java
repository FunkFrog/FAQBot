package database;

import util.Init;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SysFAQ {
    public static Optional<String> getSysFAQMessage(String command) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("SELECT Message FROM sysfaq WHERE Command = ?");
        statement.setString(1, command.toLowerCase());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return Optional.of(rs.getString("Message"));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<String> getFAQImage(String command) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("SELECT Image FROM sysfaq WHERE Command = ?");
        statement.setString(1, command.toLowerCase());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return Optional.of(rs.getString("Image"));
        } else {
            return Optional.empty();
        }
    }

    public static void addSystemImageFaq(String command, String image) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("INSERT INTO sysfaq VALUES (?, ?, ?)");
        statement.setString(1, command.toLowerCase());
        statement.setString(2, "Image FAQ.");
        statement.setString(3, image);
        statement.execute();

        Init.refreshFAQs();
    }

    public static boolean isImageEmbed(String command) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("SELECT IMAGE FROM sysfaq WHERE COMMAND = ?");
        statement.setString(1, command);
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }

    public static void addFaq(String command, String message) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("INSERT INTO SYSFAQ VALUES (?, ?, NULL)");
        statement.setString(1, command);
        statement.setString(2, message);
        statement.execute();

        Init.refreshFAQs();
    }

    public static void setFaq(String command, String message, String oldCommand) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement(
                "UPDATE faq SET COMMAND = ?, MESSAGE = ? WHERE COMMAND = ?");
        statement.setString(1, command);
        statement.setString(2, message);
        statement.setString(3, oldCommand);
        statement.execute();

        Init.refreshFAQs();
    }

    public static void removeFaq(String command) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("DELETE FROM FAQ WHERE COMMAND = ?");
        statement.setString(1, command);
        statement.execute();

        Init.refreshFAQs();
    }
}
