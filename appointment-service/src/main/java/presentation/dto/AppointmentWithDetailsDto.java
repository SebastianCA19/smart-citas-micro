package presentation.dto;

import java.time.LocalDate;

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
        LocalDate date
) {}