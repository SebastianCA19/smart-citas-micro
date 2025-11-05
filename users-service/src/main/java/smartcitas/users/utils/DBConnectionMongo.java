package smartcitas.users.utils;

import com.mongodb.client.*;

public class DBConnectionMongo {
    private MongoClient mc;

    public DBConnectionMongo() {
        setMc(MongoClients.create());
    }

    public MongoClient getMc() {
        return mc;
    }

    public void setMc(MongoClient mc) {
        this.mc = mc;
    }
}