package smartcitas.medical_records.service;

import estructura.ListaEsp;
import smartcitas.medical_records.dto.CreateMedicalRecordDTO;
import smartcitas.medical_records.dto.ResponseMedicalRecordDTO;
import smartcitas.medical_records.dto.MedicalRecordWithDetailsDTO;

public interface MedicalRecordService {
    int create(CreateMedicalRecordDTO createMedicalRecordDTO);

    int update(int idRecord, CreateMedicalRecordDTO createMedicalRecordDTO);

    ListaEsp<ResponseMedicalRecordDTO> getAll();

    ResponseMedicalRecordDTO getById(int idRecord);

    ListaEsp<ResponseMedicalRecordDTO> getByPatientId(int idPatient);

    ListaEsp<ResponseMedicalRecordDTO> getByDoctorId(int idDoctor);

    MedicalRecordWithDetailsDTO getByIdWithDetails(int idRecord);

    ListaEsp<MedicalRecordWithDetailsDTO> getAllWithDetails();

    ListaEsp<MedicalRecordWithDetailsDTO> getByPatientIdWithDetails(int idPatient);

    ListaEsp<MedicalRecordWithDetailsDTO> getByDoctorIdWithDetails(int idDoctor);
}