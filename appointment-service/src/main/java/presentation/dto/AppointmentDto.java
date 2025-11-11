package presentation.dto;

import java.time.LocalDate;

public record AppointmentDto(Integer id, Integer idAppointmentType, Integer idPlace, Integer idProcedure, Integer idDoctor, Integer idNurse, Integer idPatient, LocalDate date) {
}
