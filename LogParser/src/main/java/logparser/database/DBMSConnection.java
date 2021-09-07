package logparser.database;

import java.sql.*;
import java.util.Properties;

public class DBMSConnection {

    final String url = "jdbc:postgresql://localhost/postgres";
    private Connection connection;

    public DBMSConnection() {
        final Properties props = new Properties();
        props.setProperty("user","postgres");
        props.setProperty("password","ghantgusr");
        try {
            connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
