package database;

import util.Init;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class Database {
    static final Connection database;

    static {
        try {
            database = DriverManager.getConnection("jdbc:h2:" + System.getProperty("user.dir") + "/database;AUTO_SERVER=TRUE", "sa", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        System.out.print("Are you sure? (y/n): ");
        Scanner in = new Scanner(System.in);
        if (in.next().equalsIgnoreCase("y")) {
            resetTables();
            close();
            System.out.println("Reset!");
        } else {
            System.out.println("Canceled!");
        }
    }

    private static void close() throws SQLException {
        database.close();
    }

    public static void resetTables() throws SQLException {
        resetFAQs();
        resetSysFAQs();
    }

    private static void resetFAQs() throws SQLException {
        database.prepareStatement(
                new Scanner(Database.class.getResourceAsStream("/FAQSchema.sql")).useDelimiter("//A").next()
        ).execute();
    }

    private static void resetSysFAQs() throws SQLException {
        database.prepareStatement(
                new Scanner(Database.class.getResourceAsStream("/SysFAQSchema.sql")).useDelimiter("//A").next()
        ).execute();
    }

    public static ArrayList<String[]> getFAQs() {
        try {
            ArrayList<String[]> FAQs = new ArrayList<>();
            PreparedStatement statement = database.prepareStatement("SELECT * FROM SYSFAQ ORDER BY Command ASC");
            ResultSet rs = statement.executeQuery();
            formatFAQs(FAQs, rs);
            statement = database.prepareStatement("SELECT * FROM FAQ ORDER BY Command ASC");
            rs = statement.executeQuery();
            formatFAQs(FAQs, rs);
            // FAQs.sort(Comparator.comparing(strings -> strings[1]));
            return FAQs;
        } catch (SQLException e) {
            System.out.println("Could not fetch FAQs.");
            e.printStackTrace();
        }
        return null;
    }

    private static void formatFAQs(ArrayList<String[]> FAQs, ResultSet rs) throws SQLException {
        while (rs.next()) {
            String msg = rs.getString("Message");
            int length = msg.length() >= 19 ? 19 : msg.length();
            String preview = msg.substring(0, length) + (length >= 19 ? "..." : "");
            FAQs.add(new String[]{rs.getString("Command"), preview});
        }
    }

    public static Optional<ArrayList<String>> getUsersFAQs(Long ID) throws SQLException {
        PreparedStatement statement = database.prepareStatement("SELECT Command FROM faq WHERE ID = ?");
        statement.setLong(1, ID);
        ResultSet rs = statement.executeQuery();
        ArrayList<String> arrayList = new ArrayList<>();
        while (rs.next()) {
            arrayList.add(rs.getString("Command"));
        }
        if (arrayList.size() > 0) {
            return Optional.of(arrayList);
        } else {
            return Optional.empty();
        }
    }

    public static boolean FAQExists(String command) {
        for (int i = 0; i < Init.registeredFAQs.size(); i++) {
            if (Init.registeredFAQs.get(i)[1].equals(command)) return true;
        }
        return false;
    }
}

