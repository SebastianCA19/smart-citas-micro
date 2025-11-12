package persistence;

import estructura.ListaEsp;
import model.AppointmentType;

public interface AppointmentTypeDao {
    ListaEsp<AppointmentType> getAll();
    AppointmentType getById(int id);
}