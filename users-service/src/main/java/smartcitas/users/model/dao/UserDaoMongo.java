package smartcitas.users.model.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import smartcitas.users.model.User;

import java.util.ArrayList;
import java.util.List;

import smartcitas.users.utils.DBConnectionMongo;

import static com.mongodb.client.model.Filters.eq;

public class UserDaoMongo implements UserDao{

    DBConnectionMongo dbConn = new DBConnectionMongo();
    MongoDatabase db = dbConn.getMc().getDatabase("smartcitas");
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

        Document tempUser = (Document) users.find(eq("idUsuario", id));

        return mapDocToUser(tempUser);
    }

    @Override
    public int insert(User user) {
        users.insertOne(mapUserToDoc(user));
        return 0;
    }

    @Override
    public boolean update(User user) {
        UpdateResult out = users.updateOne(eq("idUsuario", user.getIdUsuario()), mapUserToDoc(user));

        return out.wasAcknowledged(); //not sure this works tbh
    }

    @Override
    public boolean delete(int id) {
        DeleteResult out = users.deleteOne(eq("idUsuario", id));
        return out.wasAcknowledged();
    }

    @Override
    public User login(String email, String password) {
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

    }

    public User mapDocToUser(Document doc){
        User userOut = new User();
        userOut.setIdUsuario(doc.getInteger("idUsuario"));
        userOut.setNombre(doc.getString("nombre"));
        userOut.setPrimerApellido(doc.getString("primerApellido"));
        userOut.setPrimerApellido(doc.getString("segundoApellido"));
        userOut.setEmail(doc.getString("email"));
        userOut.setClave(doc.getString("clave"));
        userOut.setEstado(doc.getInteger("estado"));
        return userOut;
    }

    public Document mapUserToDoc(User user){
        return new Document("nombre", user.getNombre())
                .append("primerApellido", user.getPrimerApellido())
                .append("segundoApellido", user.getSegundoApellido())
                .append("email", user.getEmail())
                .append("clave", user.getClave())
                .append("estado", user.getEstado());
    }

}
