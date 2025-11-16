package presentation.dto;

import java.time.LocalDateTime;

public record AppointmentDto(
        Integer id,
        Integer idAppointmentType,
        Integer idPlace,
        Integer idProcedure,
        Integer idDoctor,
        Integer idNurse,
        Integer idPatient,
        LocalDateTime date
) {}