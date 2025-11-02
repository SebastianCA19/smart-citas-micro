package smartcitas.medical_records.dto;

public record ResponseMedicalRecordDTO(int idRecord, int idPatient, int idDoctor, String diagnosis, String treatment, String notes) {
}
