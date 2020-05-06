package Communication;

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
                try {
                    ArrayList<String> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(resultSet.getObject(i).toString());
                    }
                    result.add(row);
                } catch (NullPointerException npe) {
                    System.out.println(npe.getMessage());
                }
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
            Statement statement =  conn.createStatement();
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
}
