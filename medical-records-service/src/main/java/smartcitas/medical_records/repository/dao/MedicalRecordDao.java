package smartcitas.medical_records.repository.dao;

import estructura.ListaEsp;
import smartcitas.medical_records.dto.MedicalRecordWithDetailsDTO;
import smartcitas.medical_records.repository.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordDao {
    int insert(MedicalRecord medicalRecord);
    int update(MedicalRecord medicalRecord);
    MedicalRecord findById(int idRecord);
    ListaEsp<MedicalRecord> findAll();
    ListaEsp<MedicalRecord> findByDoctorId(int idDoctor);
    ListaEsp<MedicalRecord> findByPatientId(int idPatient);

    ListaEsp<MedicalRecord> findByAppointmentId(int id);
}
