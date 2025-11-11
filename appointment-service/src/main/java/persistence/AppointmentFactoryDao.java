package persistence;

import persistence.impl.AppointmentDaoPostgres;

public class AppointmentFactoryDao {

    private static AppointmentFactoryDao instance;

    private AppointmentFactoryDao() {}

    public static AppointmentFactoryDao getInstance() {
        if (instance == null) {
            instance = new AppointmentFactoryDao();
        }
        return instance;
    }

    public AppointmentDao getDao(String dbName) {
        dbName = dbName.toUpperCase();
        switch (dbName) {
            case "POSTGRES":
                return new AppointmentDaoPostgres();
            default:
                throw new AssertionError();
        }
    }
}
