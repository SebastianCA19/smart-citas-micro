package smartcitas.citas.model;

import java.time.LocalDate;

/**
 * Entity model for appointments
 * @author edavi
 */
public class Cita implements Cloneable {

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

    public Cita() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Cita) super.clone();
    }

    @Override
    public String toString() {
        return "Cita{" + "id=" + id + ", idTipoCita=" + idTipoCita + ", idLugar=" + idLugar +
               ", idProcedimiento=" + idProcedimiento + ", idMedico=" + idMedico +
               ", idPaciente=" + idPaciente + ", idEnfermero=" + idEnfermero +
               ", fecha=" + fecha + '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTipoCita() {
        return idTipoCita;
    }

    public void setIdTipoCita(Integer idTipoCita) {
        this.idTipoCita = idTipoCita;
    }

    public Integer getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(Integer idLugar) {
        this.idLugar = idLugar;
    }

    public Integer getIdProcedimiento() {
        return idProcedimiento;
    }

    public void setIdProcedimiento(Integer idProcedimiento) {
        this.idProcedimiento = idProcedimiento;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Integer getIdEnfermero() {
        return idEnfermero;
    }

    public void setIdEnfermero(Integer idEnfermero) {
        this.idEnfermero = idEnfermero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}