package persistence.impl;

import estructura.ListaEsp;
import io.github.cdimascio.dotenv.Dotenv;
import model.Procedure;
import persistence.ProcedureDao;

import java.sql.*;

public class ProcedureDaoPostgres implements ProcedureDao {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DATABASE_URL");
    private static final String USERNAME = dotenv.get("USER");
    private static final String PASSWORD = dotenv.get("PASSWORD");
    private Connection connection;

    public ProcedureDaoPostgres() {
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
    public ListaEsp<Procedure> getAll() {
        ListaEsp<Procedure> procedures = new ListaEsp<>();
        String sql = "SELECT * FROM procedimiento";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                procedures.agregar(new Procedure(rs.getInt("id"), rs.getString("procedimiento")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting procedures", e);
        }
        return procedures;
    }

    @Override
    public Procedure getById(int id) {
        String sql = "SELECT * FROM procedimiento WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Procedure(rs.getInt("id"), rs.getString("procedimiento"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting procedure", e);
        }
        return null;
    }
}