package General;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Domotica";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";
    private static Connection connection;

    public static ArrayList<ArrayList<String>> executeQuery(String query) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                boolean addedField = false;
                for (int i = 1; i <= columnCount; i++) {
                    try {
                        row.add(resultSet.getObject(i).toString());
                        addedField = true;
                    } catch (NullPointerException npe) {
                        System.out.println(npe.getMessage());
                    }
                }
                if (addedField) result.add(row);
            }

            resultSet.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }

        return result;
    }

    public static int executeUpdate(String query) {
        int result = 0;

        try {
            Connection conn = getConnection();
            Statement statement = conn.createStatement();
            result = statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            closeConnection();
        }
        return result;
    }

    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }

        connection.setAutoCommit(true);
        return connection;
    }

    private static void closeConnection() {
        if (connection == null) {
            return;
        }

        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void updateLog(String action, String beschrijving, Integer muzieknummer) {
        String query = "INSERT INTO log (actie, beschrijving";
        if (muzieknummer != null) query += ", muzieknummer_id";
        query += ") VALUES (\"" + action + "\", \"" + beschrijving + "\"";
        if (muzieknummer != null) query += ", " + muzieknummer;
        query += ");";
        executeUpdate(query);
    }
}
