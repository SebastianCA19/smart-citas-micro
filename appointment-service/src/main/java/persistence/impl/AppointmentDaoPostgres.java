package persistence.impl;

import estructura.ListaEsp;
import io.github.cdimascio.dotenv.Dotenv;
import model.Appointment;
import persistence.AppointmentDao;

import java.sql.Connection;
import java.time.LocalDate;

public class AppointmentDaoPostgres implements AppointmentDao {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USERNAME = dotenv.get("USER");
    private static final String PASSWORD = dotenv.get("PASSWORD");

    public AppointmentDaoPostgres() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ListaEsp<Appointment> getAll() {
        return null;
    }

    @Override
    public Appointment getById(int id) {
        return null;
    }

    @Override
    public Appointment getByNameAndDate(String name, LocalDate date) {
        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void deleteByNameAndDate(String name, LocalDate date) {

    }

    @Override
    public int save(Appointment appointment) {
        return 0;
    }

    @Override
    public int update(Appointment appointment) {
        return 0;
    }
}
