package smartcitas.medical_records.service;

import estructura.ListaEsp;
import smartcitas.medical_records.dto.CreateMedicalRecordDTO;
import smartcitas.medical_records.dto.ResponseMedicalRecordDTO;



public interface MedicalRecordService {
    int create(CreateMedicalRecordDTO createMedicalRecordDTO);

    int update(int idRecord, CreateMedicalRecordDTO createMedicalRecordDTO);

    ListaEsp<ResponseMedicalRecordDTO> getAll();

    ResponseMedicalRecordDTO getById(int idRecord);

}
