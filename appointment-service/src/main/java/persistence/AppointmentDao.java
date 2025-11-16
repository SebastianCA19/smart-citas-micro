package persistence;

import estructura.ListaEsp;
import model.Appointment;

import java.time.LocalDateTime;  // Changed import

public interface AppointmentDao {
    ListaEsp<Appointment> getAll();

    Appointment getById(int id);

    Appointment getByNameAndDate(String name, LocalDateTime date);

    void deleteById(int id);

    void deleteByNameAndDate(String name, LocalDateTime date);

    int save(Appointment appointment);

    int update(Appointment appointment);
}