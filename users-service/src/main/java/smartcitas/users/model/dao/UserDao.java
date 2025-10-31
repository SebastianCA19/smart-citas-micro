package smartcitas.users.model.dao;

import smartcitas.users.model.User;

import java.util.List;

public interface UserDao{
    public List<User> getAll();
    public User findById(int id);
    public int insert(User user);
    public boolean update(User user);
    public boolean delete(int id);
    public User login(String email, String password);
    public List<User> getUsersByType(String tableName);
    public void linkUserToType(int userId, String tableName);
}
