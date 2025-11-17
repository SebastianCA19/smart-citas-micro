package smartcitas.medical_records.dto;

public record MedicalRecordWithDetailsDTO(
        int idRecord,
        int idAppointment,
        int idPatient,
        String patientName,
        int idDoctor,
        String doctorName,
        String diagnosis,
        String treatment,
        String notes
) {}