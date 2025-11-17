package smartcitas.medical_records.repository.dao;

import estructura.ListaEsp;
import smartcitas.medical_records.repository.model.MedicalRecord;
import smartcitas.medical_records.utils.DBConnection;
import java.sql.*;

public class MedicalRecordDaoPostgres implements  MedicalRecordDao {
    @Override
    public int insert(MedicalRecord medicalRecord) {
        String sql = "INSERT INTO historias_clinicas (id_cita, id_medico, diagnostico, tratamiento, notas, id_paciente) VALUES (?, ?, ?, ?, ?, ?)";

        int id = 0;

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, medicalRecord.getIdPatient());
            ps.setInt(2, medicalRecord.getIdDoctor());
            ps.setString(3, medicalRecord.getDiagnosis());
            ps.setString(4, medicalRecord.getTreatment());
            ps.setString(5, medicalRecord.getNotes());
            ps.setInt(6, medicalRecord.getIdAppointment());

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
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(MedicalRecord medicalRecord) {
        String sql = "UPDATE historias_clinicas SET id_paciente=?, id_medico=?, diagnostico=?, tratamiento=?, notas=? WHERE id_historia=?";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, medicalRecord.getIdPatient());
            ps.setInt(2, medicalRecord.getIdDoctor());
            ps.setString(3, medicalRecord.getDiagnosis());
            ps.setString(4, medicalRecord.getTreatment());
            ps.setString(5, medicalRecord.getNotes());
            ps.setInt(6, medicalRecord.getIdRecord());

            return ps.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public MedicalRecord findById(int idRecord) {
        String sql = "SELECT * FROM historias_clinicas WHERE id_historia = ?";

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idRecord);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                return mapMedicalRecord(resultSet);
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ListaEsp<MedicalRecord> findAll() {
        ListaEsp<MedicalRecord> medicalRecords = new ListaEsp<>();
        String sql = "SELECT * FROM historias_clinicas";

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    MedicalRecord medicalRecord = mapMedicalRecord(resultSet);
                    medicalRecords.agregar(medicalRecord);
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }

    @Override
    public ListaEsp<MedicalRecord> findByDoctorId(int idDoctor) {
        ListaEsp<MedicalRecord> medicalRecords = new ListaEsp<>();
        String sql = "SELECT * FROM historias_clinicas WHERE id_medico = ? ORDER BY id_historia DESC";

        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setInt(1, idDoctor);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                MedicalRecord medicalRecord = mapMedicalRecord(resultSet);
                medicalRecords.agregar(medicalRecord);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }

    @Override
    public ListaEsp<MedicalRecord> findByPatientId(int idPatient) {
        ListaEsp<MedicalRecord> medicalRecords = new ListaEsp<>();
        String sql = "SELECT * FROM historias_clinicas WHERE id_paciente = ? ORDER BY id_historia DESC";

        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(sql);){

            preparedStatement.setInt(1, idPatient);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                MedicalRecord medicalRecord = mapMedicalRecord(resultSet);
                medicalRecords.agregar(medicalRecord);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }

    public ListaEsp<MedicalRecord> findByAppointmentId(int idAppointment) {
        ListaEsp<MedicalRecord> medicalRecords = new ListaEsp<>();
        String sql = "SELECT * FROM historias_clinicas WHERE id_cita= ? ORDER BY id_historia DESC";

        try (Connection conn =  DBConnection.getInstance().getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setInt(1, idAppointment);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                MedicalRecord medicalRecord = mapMedicalRecord(resultSet);
                medicalRecords.agregar(medicalRecord);
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }

    private MedicalRecord mapMedicalRecord(ResultSet rs) throws SQLException {
        MedicalRecord mr = new MedicalRecord();
        mr.setIdRecord(rs.getInt("id_historia"));
        mr.setIdAppointment(rs.getInt("id_cita"));
        mr.setIdPatient(rs.getInt("id_paciente"));
        mr.setIdDoctor(rs.getInt("id_medico"));
        mr.setDiagnosis(rs.getString("diagnostico"));
        mr.setTreatment(rs.getString("tratamiento"));
        mr.setNotes(rs.getString("notas"));
        return mr;
    }
}
