package database;

import util.Init;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserFAQ {
    public static Optional<String> getFAQMessage(String command) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement(
                "SELECT Message FROM faq WHERE Command = ?");
        statement.setString(1, command.toLowerCase());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return Optional.of(rs.getString("Message"));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Long> getFAQOwner(String command) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement(
                "SELECT ID FROM faq WHERE Command = ?");
        statement.setString(1, command.toLowerCase());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            return Optional.of(rs.getLong("ID"));
        } else {
            return Optional.empty();
        }
    }

    public static void addUserFAQ(String command, String message, Long id) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("INSERT INTO faq VALUES (?, ?, ?)");
        statement.setLong(1, id);
        statement.setString(2, command);
        statement.setString(3, message);
        statement.execute();

        Init.refreshFAQs();
    }

    public static void setUserFAQ(String command, String message, Long id) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement(
                "UPDATE faq SET COMMAND = ?, MESSAGE = ? WHERE ID = ?");
        statement.setString(1, command);
        statement.setString(2, message);
        statement.setLong(3, id);
        statement.execute();

        Init.refreshFAQs();
    }

    public static void removeUserFAQ(Long ID) throws SQLException {
        PreparedStatement statement = Database.database.prepareStatement("DELETE FROM FAQ WHERE ID = ?");
        statement.setLong(1, ID);
        statement.execute();

        Init.refreshFAQs();
    }
}
