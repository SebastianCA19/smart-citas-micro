package smartcitas.medical_records.dto;

public record CreateMedicalRecordDTO(int idPatient, int idDoctor, String diagnosis, String treatment, String notes) {
}
