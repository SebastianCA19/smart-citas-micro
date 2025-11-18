package smartcitas.users.model.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import smartcitas.users.model.User;

import java.util.ArrayList;
import java.util.List;

import smartcitas.users.utils.DBConnectionMongo;

import static com.mongodb.client.model.Filters.*;

public class UserDaoMongo implements UserDao{

    DBConnectionMongo dbConn = new DBConnectionMongo();
    MongoDatabase db = dbConn.getMc().getDatabase("smart-citas");
    MongoCollection<Document> users = db.getCollection("users");

    @Override
    public List<User> getAll() {
        List<User> outUser = new ArrayList<>();

        for (Document doc : users.find()){

            outUser.add(mapDocToUser(doc));
        }

        return outUser;
    }

    @Override
    public User findById(int id) {
        Document tempUser = users.find(eq("cedula", id)).first();
        if (tempUser != null){
            return mapDocToUser(tempUser);
        }
        else{
            return null;
        }
    }

    @Override
    public int insert(User user) {
        InsertOneResult result = users.insertOne(mapUserToDoc(user));

        // Obtener el _id generado
        ObjectId id = result.getInsertedId().asObjectId().getValue();

        // Leer el documento recién insertado
        Document inserted = users.find(Filters.eq("_id", id)).first();

        return inserted.getInteger("cedula");
    }

    @Override
    public boolean update(User user) {
        Document tempDoc = mapUserToDoc(user);
        UpdateResult out = users.updateOne(eq("cedula", user.getCedula()), new Document("$set", tempDoc));

        return out.getModifiedCount() > 0;
    }

    @Override
    public boolean delete(int id) {
        DeleteResult out = users.deleteOne(eq("cedula", id));
        return out.getDeletedCount() > 0;
    }

    @Override
    public User login(String email, String password) {

        Document log = users.find(and(eq("email", email), eq("clave", password))).first();
        if(log != null){
            return mapDocToUser(log);
        }
        return null;
    }

    @Override
    public List<User> getUsersByType(String tableName) {

        List<User> outUser = new ArrayList<>();

        for (Document doc : users.find(eq("type",  tableName))){

            outUser.add(mapDocToUser(doc));
        }
        return outUser;
    }

    @Override
    public void linkUserToType(int userId, String tableName) {
       users.updateOne(eq("cedula", userId), new Document("$set", new Document("type", tableName)));
    }

    @Override
    public String getType() {
        return "mongo";
    }

    public User mapDocToUser(Document doc){
        User userOut = new User();
        userOut.setCedula(doc.getInteger("cedula"));
        userOut.setNombre(doc.getString("nombre"));
        userOut.setPrimerApellido(doc.getString("primerApellido"));
        userOut.setSegundoApellido(doc.getString("segundoApellido"));
        userOut.setEmail(doc.getString("email"));
        userOut.setClave(doc.getString("clave"));
        userOut.setEstado(1);
        return userOut;
    }

    public Document mapUserToDoc(User user){
        return new Document("cedula", user.getCedula())
                .append("nombre", user.getNombre())
                .append("primerApellido", user.getPrimerApellido())
                .append("segundoApellido", user.getSegundoApellido())
                .append("email", user.getEmail())
                .append("clave", user.getClave())
                .append("estado", user.getEstado());
    }

}
