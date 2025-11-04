/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import modelo.Cita;
import persistencia.CitaDao;
import persistencia.FactoryCitaDao;
import presentacion.dto.CitaDto;

/**
 *
 * @author edavi
 */
public class CitaService {
    
    private FactoryCitaDao facto = FactoryCitaDao.getInstance();
    
    public int crearCita(CitaDto dto){
        CitaDao dao = facto.getCitaDao("POSTGRES");
        Cita cita = new Cita(dto.idTipoCita(), dto.idLugar(), dto.idProcedimiento(),
                dto.idMedico(), dto.idEnfermero() ,dto.idPaciente(), dto.fecha());
        return dao.crear(cita);
    }
    
}
