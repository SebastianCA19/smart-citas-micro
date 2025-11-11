package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Appointment implements Cloneable{
    private Integer id;
    private Integer idAppointmentType;
    private Integer idPlace;
    private Integer idProcedure;
    private Integer idDoctor;
    private Integer idNurse;
    private Integer idPatient;
    private LocalDate date;

    public Appointment(
            Integer id, Integer idAppointmentType, Integer idPlace, Integer idNurse, Integer idProcedure, Integer idDoctor, Integer idPatient, LocalDate date
    ) {
        this.id = id;
        this.idAppointmentType = idAppointmentType;
        this.idPlace = idPlace;
        this.idProcedure = idProcedure;
        this.idDoctor = idDoctor;
        this.idNurse = idNurse;
        this.idPatient = idPatient;
        this.date = date;
    }

    @Override
    public Appointment clone() throws CloneNotSupportedException {
        return (Appointment) super.clone();
    }

    @Override
    public String toString() {
        return "Cita{" + "id=" + id + ", idTipoCita=" + idAppointmentType + ", idLugar=" + idPlace + ", idProcedimiento=" + idProcedure + ", idMedico=" + idDoctor + ", idPaciente=" + idPatient + ", idEnfermero=" + idNurse + ", fecha=" + date + '}';
    }

    public Integer getIdAppointmentType() {
        return idAppointmentType;
    }

    public void setIdAppointmentType(Integer idAppointmentType) {
        this.idAppointmentType = idAppointmentType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(Integer idPlace) {
        this.idPlace = idPlace;
    }

    public Integer getIdProcedure() {
        return idProcedure;
    }

    public void setIdProcedure(Integer idProcedure) {
        this.idProcedure = idProcedure;
    }

    public Integer getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(Integer idDoctor) {
        this.idDoctor = idDoctor;
    }

    public Integer getIdNurse() {
        return idNurse;
    }

    public void setIdNurse(Integer idNurse) {
        this.idNurse = idNurse;
    }

    public Integer getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(Integer idPatient) {
        this.idPatient = idPatient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
