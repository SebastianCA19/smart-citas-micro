package smartcitas.users.model.dao;

import smartcitas.users.model.User;

import java.util.List;

public interface UserDao{
    List<User> getAll();
    User findById(int id);
    int insert(User user);
    boolean update(User user);
    boolean delete(int id);
    User login(String email, String password);
    List<User> getUsersByType(String tableName);
    void linkUserToType(int userId, String tableName);
    String getType();
}
