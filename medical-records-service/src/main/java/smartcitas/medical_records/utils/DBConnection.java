package smartcitas.medical_records.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private final Dotenv dotenv = Dotenv.load();

    private final String URL = dotenv.get("DATABASE_URL");
    private final String USER = dotenv.get("USER");
    private final String PASSWORD = dotenv.get("PASSWORD");

    private Connection connection;

    private DBConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connect();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading PostgreSQL Driver", e);
        }
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            connect();
        }
        return connection;
    }
}

