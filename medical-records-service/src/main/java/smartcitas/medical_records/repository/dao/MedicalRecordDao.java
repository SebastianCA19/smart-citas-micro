package smartcitas.medical_records.repository.dao;

import smartcitas.medical_records.repository.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordDao {
    int insert(MedicalRecord medicalRecord);
    int update(MedicalRecord medicalRecord);
    MedicalRecord findById(int idRecord);
    List<MedicalRecord> findAll();

}
