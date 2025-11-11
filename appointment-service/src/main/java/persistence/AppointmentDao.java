package persistence;

import estructura.ListaEsp;
import model.Appointment;

import java.time.LocalDate;

public interface AppointmentDao {
    ListaEsp<Appointment> getAll();

    Appointment getById(int id);

    Appointment getByNameAndDate(String name, LocalDate date);

    void deleteById(int id);

    void deleteByNameAndDate(String name, LocalDate date);

    int save(Appointment appointment);

    int update(Appointment appointment);
}
