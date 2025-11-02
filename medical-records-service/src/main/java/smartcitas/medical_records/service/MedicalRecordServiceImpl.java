package smartcitas.medical_records.service;

import smartcitas.medical_records.dto.CreateMedicalRecordDTO;
import smartcitas.medical_records.dto.ResponseMedicalRecordDTO;
import smartcitas.medical_records.repository.dao.MedicalRecordDao;
import smartcitas.medical_records.repository.dao.MedicalRecordDaoPostgres;
import smartcitas.medical_records.repository.model.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordDao dao = new MedicalRecordDaoPostgres();

    @Override
    public int create(CreateMedicalRecordDTO createMedicalRecordDTO) {
        MedicalRecord medicalRecord = new MedicalRecord(0, createMedicalRecordDTO.idPatient(),
                createMedicalRecordDTO.idDoctor(),
                createMedicalRecordDTO.diagnosis(),
                createMedicalRecordDTO.treatment(),
                createMedicalRecordDTO.notes());

        return dao.insert(medicalRecord);
    }

    @Override
    public int update(int idRecord, CreateMedicalRecordDTO createMedicalRecordDTO) {
        MedicalRecord foundRecord = dao.findById(idRecord);
        if (foundRecord != null) {
            foundRecord.setIdPatient(createMedicalRecordDTO.idPatient());
            foundRecord.setIdDoctor(createMedicalRecordDTO.idDoctor());
            foundRecord.setDiagnosis(createMedicalRecordDTO.diagnosis());
            foundRecord.setTreatment(createMedicalRecordDTO.treatment());
            foundRecord.setNotes(createMedicalRecordDTO.notes());

            return dao.update(foundRecord);
        }
        return 0;
    }

    @Override
    public List<ResponseMedicalRecordDTO> getAll() {
        List<MedicalRecord> medicalRecords = dao.findAll();
        List<ResponseMedicalRecordDTO> responseMedicalRecordDTOList = new ArrayList<>();
        for (MedicalRecord medicalRecord : medicalRecords) {
            ResponseMedicalRecordDTO responseDTO = new ResponseMedicalRecordDTO(
                    medicalRecord.getIdRecord(),
                    medicalRecord.getIdPatient(),
                    medicalRecord.getIdDoctor(),
                    medicalRecord.getDiagnosis(),
                    medicalRecord.getTreatment(),
                    medicalRecord.getNotes()
            );
            responseMedicalRecordDTOList.add(responseDTO);
        }
        return responseMedicalRecordDTOList;
    }

    @Override
    public ResponseMedicalRecordDTO getById(int idRecord) {
        MedicalRecord medicalRecord = dao.findById(idRecord);
        if (medicalRecord != null) {
            return new ResponseMedicalRecordDTO(
                    medicalRecord.getIdRecord(),
                    medicalRecord.getIdPatient(),
                    medicalRecord.getIdDoctor(),
                    medicalRecord.getDiagnosis(),
                    medicalRecord.getTreatment(),
                    medicalRecord.getNotes()
            );
        }
        return null;
    }

}
