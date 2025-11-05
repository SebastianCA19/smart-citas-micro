package smartcitas.users.model.dao;

public class DAOFactory {
    private static DAOFactory fac = null;

    public static DAOFactory getFactory() {
        if (fac == null) {
            fac = new DAOFactory();
        }
        return fac;
    }

    public static UserDao getUserDao(String inter) {
        switch (inter) {
            case "postgres":
                UserDaoPostgres dp = new UserDaoPostgres();
                return (UserDao) dp;
            case "mongo":
                UserDaoMongo dm = new UserDaoMongo();
                return (UserDao) dm;
            default:
                return null;
        }
    }
}
