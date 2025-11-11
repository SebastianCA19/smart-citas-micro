package smartcitas.citas.dao;

import smartcitas.citas.dao.impl.CitaDaoPostgres;

/**
 * Factory class for creating CitaDao instances
 * @author edavi
 */
public class FactoryCitaDao {

    private static FactoryCitaDao instance;

    private FactoryCitaDao() {
    }

    public static FactoryCitaDao getInstance() {
        if (instance == null) {
            instance = new FactoryCitaDao();
        }
        return instance;
    }

    public CitaDao getCitaDao(String nombreDb) {
        nombreDb = nombreDb.toUpperCase();
        switch (nombreDb) {
            case "POSTGRES":
                return new CitaDaoPostgres();
            default:
                throw new IllegalArgumentException("Unsupported database: " + nombreDb);
        }
    }
}