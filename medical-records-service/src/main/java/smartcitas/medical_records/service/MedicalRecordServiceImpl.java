package smartcitas.medical_records.service;

import estructura.ListaEsp;
import smartcitas.medical_records.dto.CreateMedicalRecordDTO;
import smartcitas.medical_records.dto.ResponseMedicalRecordDTO;
import smartcitas.medical_records.dto.MedicalRecordWithDetailsDTO;
import smartcitas.medical_records.repository.dao.MedicalRecordDao;
import smartcitas.medical_records.repository.dao.MedicalRecordDaoPostgres;
import smartcitas.medical_records.repository.model.MedicalRecord;

public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordDao dao = new MedicalRecordDaoPostgres();
    private final UserServiceClient userServiceClient = new UserServiceClient();

    @Override
    public int create(CreateMedicalRecordDTO createMedicalRecordDTO) {
        MedicalRecord medicalRecord = new MedicalRecord(
                0,
                createMedicalRecordDTO.idPatient(),
                createMedicalRecordDTO.idDoctor(),
                createMedicalRecordDTO.diagnosis(),
                createMedicalRecordDTO.treatment(),
                createMedicalRecordDTO.notes()
        );

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
    public ListaEsp<ResponseMedicalRecordDTO> getAll() {
        ListaEsp<MedicalRecord> medicalRecords = dao.findAll();
        ListaEsp<ResponseMedicalRecordDTO> responseMedicalRecordDTOList = new ListaEsp<>();

        for (int i = 0; i < medicalRecords.numElementos(); i++) {
            MedicalRecord mr = medicalRecords.obtener(i);
            ResponseMedicalRecordDTO responseDTO = new ResponseMedicalRecordDTO(
                    mr.getIdRecord(),
                    mr.getIdPatient(),
                    mr.getIdDoctor(),
                    mr.getDiagnosis(),
                    mr.getTreatment(),
                    mr.getNotes()
            );
            responseMedicalRecordDTOList.agregar(responseDTO);
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

    @Override
    public ListaEsp<ResponseMedicalRecordDTO> getByPatientId(int idPatient) {
        ListaEsp<MedicalRecord> medicalRecords = dao.findByPatientId(idPatient);
        ListaEsp<ResponseMedicalRecordDTO> responseMedicalRecordDTOList = new ListaEsp<>();

        for (int i = 0; i < medicalRecords.numElementos(); i++) {
            MedicalRecord mr = medicalRecords.obtener(i);
            ResponseMedicalRecordDTO responseDTO = new ResponseMedicalRecordDTO(
                    mr.getIdRecord(),
                    mr.getIdPatient(),
                    mr.getIdDoctor(),
                    mr.getDiagnosis(),
                    mr.getTreatment(),
                    mr.getNotes()
            );
            responseMedicalRecordDTOList.agregar(responseDTO);
        }
        return responseMedicalRecordDTOList;
    }

    @Override
    public ListaEsp<ResponseMedicalRecordDTO> getByDoctorId(int idDoctor) {
        ListaEsp<MedicalRecord> medicalRecords = dao.findByDoctorId(idDoctor);
        ListaEsp<ResponseMedicalRecordDTO> responseMedicalRecordDTOList = new ListaEsp<>();

        for (int i = 0; i < medicalRecords.numElementos(); i++) {
            MedicalRecord mr = medicalRecords.obtener(i);
            ResponseMedicalRecordDTO responseDTO = new ResponseMedicalRecordDTO(
                    mr.getIdRecord(),
                    mr.getIdPatient(),
                    mr.getIdDoctor(),
                    mr.getDiagnosis(),
                    mr.getTreatment(),
                    mr.getNotes()
            );
            responseMedicalRecordDTOList.agregar(responseDTO);
        }
        return responseMedicalRecordDTOList;
    }

    @Override
    public MedicalRecordWithDetailsDTO getByIdWithDetails(int idRecord) {
        MedicalRecord medicalRecord = dao.findById(idRecord);
        if (medicalRecord != null) {
            String patientName = userServiceClient.getUserFullName(medicalRecord.getIdPatient());
            String doctorName = userServiceClient.getUserFullName(medicalRecord.getIdDoctor());

            return new MedicalRecordWithDetailsDTO(
                    medicalRecord.getIdRecord(),
                    medicalRecord.getIdPatient(),
                    patientName,
                    medicalRecord.getIdDoctor(),
                    doctorName,
                    medicalRecord.getDiagnosis(),
                    medicalRecord.getTreatment(),
                    medicalRecord.getNotes()
            );
        }
        return null;
    }

    @Override
    public ListaEsp<MedicalRecordWithDetailsDTO> getAllWithDetails() {
        ListaEsp<MedicalRecord> medicalRecords = dao.findAll();
        ListaEsp<MedicalRecordWithDetailsDTO> detailedRecords = new ListaEsp<>();

        for (int i = 0; i < medicalRecords.numElementos(); i++) {
            MedicalRecord mr = medicalRecords.obtener(i);
            String patientName = userServiceClient.getUserFullName(mr.getIdPatient());
            String doctorName = userServiceClient.getUserFullName(mr.getIdDoctor());

            MedicalRecordWithDetailsDTO detailDTO = new MedicalRecordWithDetailsDTO(
                    mr.getIdRecord(),
                    mr.getIdPatient(),
                    patientName,
                    mr.getIdDoctor(),
                    doctorName,
                    mr.getDiagnosis(),
                    mr.getTreatment(),
                    mr.getNotes()
            );
            detailedRecords.agregar(detailDTO);
        }
        return detailedRecords;
    }

    @Override
    public ListaEsp<MedicalRecordWithDetailsDTO> getByPatientIdWithDetails(int idPatient) {
        ListaEsp<MedicalRecord> medicalRecords = dao.findByPatientId(idPatient);
        ListaEsp<MedicalRecordWithDetailsDTO> detailedRecords = new ListaEsp<>();

        for (int i = 0; i < medicalRecords.numElementos(); i++) {
            MedicalRecord mr = medicalRecords.obtener(i);
            String patientName = userServiceClient.getUserFullName(mr.getIdPatient());
            String doctorName = userServiceClient.getUserFullName(mr.getIdDoctor());

            MedicalRecordWithDetailsDTO detailDTO = new MedicalRecordWithDetailsDTO(
                    mr.getIdRecord(),
                    mr.getIdPatient(),
                    patientName,
                    mr.getIdDoctor(),
                    doctorName,
                    mr.getDiagnosis(),
                    mr.getTreatment(),
                    mr.getNotes()
            );
            detailedRecords.agregar(detailDTO);
        }
        return detailedRecords;
    }

    @Override
    public ListaEsp<MedicalRecordWithDetailsDTO> getByDoctorIdWithDetails(int idDoctor) {
        ListaEsp<MedicalRecord> medicalRecords = dao.findByDoctorId(idDoctor);
        ListaEsp<MedicalRecordWithDetailsDTO> detailedRecords = new ListaEsp<>();

        for (int i = 0; i < medicalRecords.numElementos(); i++) {
            MedicalRecord mr = medicalRecords.obtener(i);
            String patientName = userServiceClient.getUserFullName(mr.getIdPatient());
            String doctorName = userServiceClient.getUserFullName(mr.getIdDoctor());

            MedicalRecordWithDetailsDTO detailDTO = new MedicalRecordWithDetailsDTO(
                    mr.getIdRecord(),
                    mr.getIdPatient(),
                    patientName,
                    mr.getIdDoctor(),
                    doctorName,
                    mr.getDiagnosis(),
                    mr.getTreatment(),
                    mr.getNotes()
            );
            detailedRecords.agregar(detailDTO);
        }
        return detailedRecords;
    }
}