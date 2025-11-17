package smartcitas.medical_records.dto;

public record CreateMedicalRecordDTO(int idAppointment, int idPatient, int idDoctor, String diagnosis, String treatment, String notes) {
}
