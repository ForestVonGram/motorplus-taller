package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/motorplus";
    private static final String DEFAULT_USER = "forest";
    private static final String DEFAULT_PASSWORD = "";

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties props = new Properties();
        String url = DEFAULT_URL;
        String user = DEFAULT_USER;
        String password = DEFAULT_PASSWORD;
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                props.load(input);
                url = props.getProperty("db.url", DEFAULT_URL);
                user = props.getProperty("db.user", DEFAULT_USER);
                password = props.getProperty("db.password", DEFAULT_PASSWORD);
            }
        } catch (Exception ignored) {
            // Fallback to defaults if properties cannot be loaded
        }
        URL = url;
        USER = user;
        PASSWORD = password;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
