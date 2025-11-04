/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author edavi
 */
public class Cita implements Cloneable{
    
    private Integer id;
    private Integer idTipoCita;
    private Integer idLugar;
    private Integer idProcedimiento;
    private Integer idMedico;
    private Integer idEnfermero;
    private Integer idPaciente;
    private LocalDate fecha;

    public Cita(Integer idTipoCita, Integer idLugar, Integer idProcedimiento, Integer idMedico, Integer idEnfermero, Integer idPaciente, LocalDate fecha) {
        this.idTipoCita = idTipoCita;
        this.idLugar = idLugar;
        this.idProcedimiento = idProcedimiento;
        this.idMedico = idMedico;
        this.idEnfermero = idEnfermero;
        this.idPaciente = idPaciente;
        this.fecha = fecha;
    }

  

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Cita) super.clone(); 
    }
    
    

    @Override
    public String toString() {
        return "Cita{" + "id=" + id + ", idTipoCita=" + idTipoCita + ", idLugar=" + idLugar + ", idProcedimiento=" + idProcedimiento + ", idMedico=" + idMedico + ", idPaciente=" + idPaciente + ", idEnfermero=" + idEnfermero + ", fecha=" + fecha + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the idTipoCita
     */
    public Integer getIdTipoCita() {
        return idTipoCita;
    }

    /**
     * @param idTipoCita the idTipoCita to set
     */
    public void setIdTipoCita(Integer idTipoCita) {
        this.idTipoCita = idTipoCita;
    }

    /**
     * @return the idLugar
     */
    public Integer getIdLugar() {
        return idLugar;
    }

    /**
     * @param idLugar the idLugar to set
     */
    public void setIdLugar(Integer idLugar) {
        this.idLugar = idLugar;
    }

    /**
     * @return the idProcedimiento
     */
    public Integer getIdProcedimiento() {
        return idProcedimiento;
    }

    /**
     * @param idProcedimiento the idProcedimiento to set
     */
    public void setIdProcedimiento(Integer idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    /**
     * @return the idMedico
     */
    public Integer getIdMedico() {
        return idMedico;
    }

    /**
     * @param idMedico the idMedico to set
     */
    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    /**
     * @return the idPaciente
     */
    public Integer getIdPaciente() {
        return idPaciente;
    }

    /**
     * @param idPaciente the idPaciente to set
     */
    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    /**
     * @return the idEnfermero
     */
    public Integer getIdEnfermero() {
        return idEnfermero;
    }

    /**
     * @param idEnfermero the idEnfermero to set
     */
    public void setIdEnfermero(Integer idEnfermero) {
        this.idEnfermero = idEnfermero;
    }

    /**
     * @return the fecha
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    
    
}
