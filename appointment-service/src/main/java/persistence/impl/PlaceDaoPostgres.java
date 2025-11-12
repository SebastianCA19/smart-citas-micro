package persistence.impl;

import estructura.ListaEsp;
import io.github.cdimascio.dotenv.Dotenv;
import model.Place;
import persistence.PlaceDao;

import java.sql.*;

public class PlaceDaoPostgres implements PlaceDao {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DATABASE_URL");
    private static final String USERNAME = dotenv.get("USER");
    private static final String PASSWORD = dotenv.get("PASSWORD");
    private Connection connection;

    public PlaceDaoPostgres() {
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
    public ListaEsp<Place> getAll() {
        ListaEsp<Place> places = new ListaEsp<>();
        String sql = "SELECT * FROM lugar";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                places.agregar(new Place(rs.getInt("id"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting places", e);
        }
        return places;
    }

    @Override
    public Place getById(int id) {
        String sql = "SELECT * FROM lugar WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Place(rs.getInt("id"), rs.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting place", e);
        }
        return null;
    }
}