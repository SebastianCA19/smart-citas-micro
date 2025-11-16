package persistence.impl;

import estructura.ListaEsp;
import io.github.cdimascio.dotenv.Dotenv;
import model.Appointment;
import persistence.AppointmentDao;

import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentDaoPostgres implements AppointmentDao {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DATABASE_URL");
    private static final String USERNAME = dotenv.get("USER");
    private static final String PASSWORD = dotenv.get("PASSWORD");
    private Connection connection;

    public AppointmentDaoPostgres() {
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
    public ListaEsp<Appointment> getAll() {
        ListaEsp<Appointment> appointments = new ListaEsp<>();
        String sql = "SELECT * FROM citas";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Appointment appointment = mapAppointment(rs);
                appointments.agregar(appointment);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all appointments", e);
        }

        return appointments;
    }

    @Override
    public Appointment getById(int id) {
        String sql = "SELECT * FROM citas WHERE id_cita = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapAppointment(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting appointment by id", e);
        }

        return null;
    }

    @Override
    public Appointment getByNameAndDate(String name, LocalDateTime date) {
        String sql = "SELECT c.* FROM citas c " +
                "INNER JOIN usuarios u ON c.id_paciente = u.cedula " +
                "WHERE u.nombre = ? AND c.fecha = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setTimestamp(2, Timestamp.valueOf(date));  // Changed to Timestamp

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapAppointment(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting appointment by name and date", e);
        }

        return null;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM citas WHERE id_cita = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment by id", e);
        }
    }

    @Override
    public void deleteByNameAndDate(String name, LocalDateTime date) {
        String sql = "DELETE FROM citas WHERE id_cita IN " +
                "(SELECT c.id_cita FROM citas c " +
                "INNER JOIN usuarios u ON c.id_paciente = u.cedula " +
                "WHERE u.nombre = ? AND c.fecha = ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setTimestamp(2, Timestamp.valueOf(date));  // Changed to Timestamp
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting appointment by name and date", e);
        }
    }

    @Override
    public int save(Appointment appointment) {
        String sql = "INSERT INTO citas (id_tipo_cita, id_lugar, id_procedimiento, id_medico, id_enfermero, id_paciente, fecha) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int id = 0;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, appointment.getIdAppointmentType());
            ps.setInt(2, appointment.getIdPlace());
            ps.setInt(3, appointment.getIdProcedure());
            ps.setInt(4, appointment.getIdDoctor());
            ps.setInt(5, appointment.getIdNurse());
            ps.setInt(6, appointment.getIdPatient());
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getDate()));  // Changed to Timestamp

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }

            return id;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    @Override
    public int update(Appointment appointment) {
        String sql = "UPDATE citas SET id_tipo_cita=?, id_lugar=?, id_procedimiento=?, " +
                "id_medico=?, id_enfermero=?, id_paciente=?, fecha=? WHERE id_cita=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, appointment.getIdAppointmentType());
            ps.setInt(2, appointment.getIdPlace());
            ps.setInt(3, appointment.getIdProcedure());
            ps.setInt(4, appointment.getIdDoctor());
            ps.setInt(5, appointment.getIdNurse());
            ps.setInt(6, appointment.getIdPatient());
            ps.setTimestamp(7, Timestamp.valueOf(appointment.getDate()));  // Changed to Timestamp
            ps.setInt(8, appointment.getId());

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating appointment", e);
        }
    }

    public ListaEsp<Appointment> getByPatientId(int patientId) {
        ListaEsp<Appointment> appointments = new ListaEsp<>();
        String sql = "SELECT * FROM citas WHERE id_paciente = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appointment = mapAppointment(rs);
                appointments.agregar(appointment);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting appointments by patient id", e);
        }

        return appointments;
    }

    public String getAppointmentTypeName(int id) {
        String sql = "SELECT nombre FROM tipo_cita WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting appointment type name", e);
        }
        return null;
    }

    public String getProcedureName(int id) {
        String sql = "SELECT procedimiento FROM procedimiento WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("procedimiento");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting procedure name", e);
        }
        return null;
    }

    public String getPlaceName(int id) {
        String sql = "SELECT nombre FROM lugar WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting place name", e);
        }
        return null;
    }

    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("id_cita"),
                rs.getInt("id_tipo_cita"),
                rs.getInt("id_lugar"),
                rs.getInt("id_enfermero"),
                rs.getInt("id_procedimiento"),
                rs.getInt("id_medico"),
                rs.getInt("id_paciente"),
                rs.getTimestamp("fecha").toLocalDateTime()
        );
    }
}