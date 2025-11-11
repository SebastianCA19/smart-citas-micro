package smartcitas.citas.dao.impl;

import smartcitas.citas.dao.CitaDao;
import smartcitas.citas.model.Cita;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of CitaDao
 * @author edavi
 */
public class CitaDaoPostgres implements CitaDao {

    private String URL;
    private String USER;
    private String PASSWORD;

    public CitaDaoPostgres() {
        loadConfiguration();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    private void loadConfiguration() {
        try {
            java.io.InputStream inputStream = getClass().getClassLoader().getResourceAsStream(".env");
            if (inputStream == null) {
                throw new RuntimeException(".env file not found in classpath");
            }

            java.util.Properties props = new java.util.Properties();
            props.load(inputStream);

            this.URL = props.getProperty("DATABASE_URL");
            this.USER = props.getProperty("USER");
            this.PASSWORD = props.getProperty("PASSWORD");

            if (URL == null || USER == null || PASSWORD == null) {
                throw new RuntimeException("Missing required database configuration in .env file. Required: DATABASE_URL, USER, PASSWORD");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    @Override
    public List<Cita> obtenerTodos() {
        List<Cita> citas = new ArrayList<>();
        String sql = "SELECT id, idTipoCita, idLugar, idProcedimiento, idMedico, idEnfermero, idPaciente, fecha FROM citas";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita cita = mapResultSetToCita(rs);
                citas.add(cita);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all appointments", e);
        }

        return citas;
    }

    @Override
    public Optional<Cita> obtenerPorId(Integer id) {
        String sql = "SELECT id, idTipoCita, idLugar, idProcedimiento, idMedico, idEnfermero, idPaciente, fecha FROM citas WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCita(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointment with id: " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public int crear(Cita cita) {
        String sql = "INSERT INTO citas (idTipoCita, idLugar, idProcedimiento, idPaciente, idMedico, idEnfermero, fecha) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cita.getIdTipoCita());
            ps.setInt(2, cita.getIdLugar());
            ps.setInt(3, cita.getIdProcedimiento());
            ps.setInt(4, cita.getIdPaciente());
            ps.setInt(5, cita.getIdMedico());
            ps.setInt(6, cita.getIdEnfermero());
            ps.setObject(7, cita.getFecha());

            int result = ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cita.setId(generatedKeys.getInt(1));
                }
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating appointment", e);
        }
    }

    @Override
    public boolean editar(Cita cita) {
        String sql = "UPDATE citas SET idTipoCita = ?, idLugar = ?, idProcedimiento = ?, idPaciente = ?, idMedico = ?, idEnfermero = ?, fecha = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cita.getIdTipoCita());
            ps.setInt(2, cita.getIdLugar());
            ps.setInt(3, cita.getIdProcedimiento());
            ps.setInt(4, cita.getIdPaciente());
            ps.setInt(5, cita.getIdMedico());
            ps.setInt(6, cita.getIdEnfermero());
            ps.setObject(7, cita.getFecha());
            ps.setInt(8, cita.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment with id: " + cita.getId(), e);
        }
    }

    @Override
    public boolean eliminarPorId(Integer id) {
        String sql = "DELETE FROM citas WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment with id: " + id, e);
        }
    }

    @Override
    public Optional<Cita> obtenerPorNombreYFecha(String nombre, LocalDate fecha) {
        String sql = "SELECT c.id, c.idTipoCita, c.idLugar, c.idProcedimiento, c.idMedico, c.idEnfermero, c.idPaciente, c.fecha " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.idPaciente = p.id " +
                     "WHERE p.nombre = ? AND c.fecha = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setObject(2, fecha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCita(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointment by name and date", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean eliminarPorNombreYFecha(String nombre, LocalDate fecha) {
        String sql = "DELETE FROM citas " +
                     "WHERE id IN (" +
                     "SELECT c.id FROM citas c " +
                     "JOIN pacientes p ON c.idPaciente = p.id " +
                     "WHERE p.nombre = ? AND c.fecha = ?" +
                     ")";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setObject(2, fecha);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment by name and date", e);
        }
    }

    private Cita mapResultSetToCita(ResultSet rs) throws SQLException {
        Cita cita = new Cita();
        cita.setId(rs.getInt("id"));
        cita.setIdTipoCita(rs.getInt("idTipoCita"));
        cita.setIdLugar(rs.getInt("idLugar"));
        cita.setIdProcedimiento(rs.getInt("idProcedimiento"));
        cita.setIdMedico(rs.getInt("idMedico"));
        cita.setIdEnfermero(rs.getInt("idEnfermero"));
        cita.setIdPaciente(rs.getInt("idPaciente"));
        cita.setFecha(rs.getObject("fecha", LocalDate.class));
        return cita;
    }
}