package smartcitas.citas.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for Cita entity
 * @author edavi
 */
public record CitaDto(Integer idTipoCita, Integer idLugar, Integer idProcedimiento,
                      Integer idPaciente, Integer idMedico, Integer idEnfermero, LocalDate fecha) {
}