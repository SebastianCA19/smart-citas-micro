/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.dto;
import java.time.LocalDate;
/**
 *
 * @author edavi
 */
public record CitaDto(Integer idTipoCita ,Integer idLugar ,Integer idProcedimiento, Integer idPaciente, 
            Integer idMedico, Integer idEnfermero, LocalDate fecha) {
    
}
