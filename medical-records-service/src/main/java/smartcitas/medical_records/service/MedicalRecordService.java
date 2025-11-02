package smartcitas.medical_records.service;

import smartcitas.medical_records.dto.CreateMedicalRecordDTO;
import smartcitas.medical_records.dto.ResponseMedicalRecordDTO;


import java.util.List;

public interface MedicalRecordService {
    int create(CreateMedicalRecordDTO createMedicalRecordDTO);

    int update(int idRecord, CreateMedicalRecordDTO createMedicalRecordDTO);

    List<ResponseMedicalRecordDTO> getAll();

    ResponseMedicalRecordDTO getById(int idRecord);

}
