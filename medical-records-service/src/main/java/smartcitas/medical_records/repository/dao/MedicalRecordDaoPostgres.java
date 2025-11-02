package smartcitas.medical_records.repository.dao;

import smartcitas.medical_records.repository.model.MedicalRecord;
import smartcitas.medical_records.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDaoPostgres implements  MedicalRecordDao {
    @Override
    public int insert(MedicalRecord medicalRecord) {
        String sql = "INSERT INTO medical_records (id_patient, id_doctor, diagnosis, treatment, notes) VALUES (?, ?, ?, ?, ?)";

        int id = 0;

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, medicalRecord.getIdPatient());
            ps.setInt(2, medicalRecord.getIdDoctor());
            ps.setString(3, medicalRecord.getDiagnosis());
            ps.setString(4, medicalRecord.getTreatment());
            ps.setString(5, medicalRecord.getNotes());

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
        String sql = "UPDATE historias_clinica SET id_patient=?, id_doctor=?, diagnosis=?, treatment=?, notes=? WHERE id_record=?";

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
        String sql = "SELECT * FROM historias_clinica WHERE id_record = ?";

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
    public List<MedicalRecord> findAll() {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        String sql = "SELECT * FROM historias_clinica";

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    MedicalRecord medicalRecord = mapMedicalRecord(resultSet);
                    medicalRecords.add(medicalRecord);
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return medicalRecords;
    }

    private MedicalRecord mapMedicalRecord(ResultSet rs) throws SQLException {
        MedicalRecord mr = new MedicalRecord();
        mr.setIdRecord(rs.getInt("id_record"));
        mr.setIdPatient(rs.getInt("id_patient"));
        mr.setIdDoctor(rs.getInt("id_doctor"));
        mr.setDiagnosis(rs.getString("diagnosis"));
        mr.setTreatment(rs.getString("treatment"));
        mr.setNotes(rs.getString("notes"));
        return mr;
    }
}
