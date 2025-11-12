package persistence.impl;

import estructura.ListaEsp;
import io.github.cdimascio.dotenv.Dotenv;
import model.AppointmentType;
import persistence.AppointmentTypeDao;

import java.sql.*;

public class AppointmentTypeDaoPostgres implements AppointmentTypeDao {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DATABASE_URL");
    private static final String USERNAME = dotenv.get("USER");
    private static final String PASSWORD = dotenv.get("PASSWORD");
    private Connection connection;

    public AppointmentTypeDaoPostgres() {
        try {
            Class.forName("org.postgresql.Driver");
            connect();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading PostgreSQL Driver", e);
        }
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        }
    }

    private synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            connect();
        }
        return connection;
    }

    @Override
    public ListaEsp<AppointmentType> getAll() {
        ListaEsp<AppointmentType> types = new ListaEsp<>();
        String sql = "SELECT * FROM tipo_cita";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                types.agregar(new AppointmentType(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting appointment types", e);
        }
        return types;
    }

    @Override
    public AppointmentType getById(int id) {
        String sql = "SELECT * FROM tipo_cita WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new AppointmentType(rs.getInt("id"), rs.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting appointment type", e);
        }
        return null;
    }
}