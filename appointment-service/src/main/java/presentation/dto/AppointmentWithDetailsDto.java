package presentation.dto;

import java.time.LocalDateTime;

public record AppointmentWithDetailsDto(
        Integer id,
        Integer idAppointmentType,
        String appointmentTypeName,
        Integer idPlace,
        String placeName,
        Integer idProcedure,
        String procedureName,
        Integer idDoctor,
        String doctorName,
        Integer idNurse,
        Integer idPatient,
        LocalDateTime date
) {}