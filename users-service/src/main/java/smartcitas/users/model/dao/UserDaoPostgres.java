package smartcitas.users.model.dao;

import smartcitas.users.model.User;
import smartcitas.users.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoPostgres implements UserDao {
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from usuarios";

        try(Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();){
                while(resultSet.next()){
                    User user = mapUser(resultSet);
                    users.add(user);
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    @Override
    public User findById(int id) {
        String sql = "select * from usuarios where id_usuario = ?";
        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();

            if(resultSet.next()){
                return mapUser(resultSet);
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int insert(User user) {
        String sql = "INSERT INTO usuarios (nombre, primer_apellido, segundo_apellido, email, clave, estado) VALUES (?, ?, ?, ?, ?, ?)";

        int id = 0;

        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getPrimerApellido());
            ps.setString(3, user.getSegundoApellido());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getClave());
            ps.setInt(6, user.getEstado());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getInt(1);
                    }
                }
            }

            return id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE usuarios SET nombre=?, primer_apellido=?, segundo_apellido=?, email=?, clave=?, estado=? WHERE id_usuario=?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getNombre());
            ps.setString(2, user.getPrimerApellido());
            ps.setString(3, user.getSegundoApellido());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getClave());
            ps.setInt(6, user.getEstado());
            ps.setInt(7, user.getIdUsuario());

            return ps.executeUpdate() > 0;
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "UPDATE usuarios SET estado=0 WHERE id_usuario=?";

        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public User login(String email, String password) {
        String sql  = "SELECT * FROM usuarios WHERE email=? AND clave=?";

        try(Connection connection = DBConnection.getInstance().getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return mapUser(resultSet);
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<User> getUsersByType(String tableName) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.* FROM usuarios u " +
                "INNER JOIN " + tableName + " t ON u.id_usuario = t.id_usuario";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = mapUser(rs);
                list.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void linkUserToType(int userId, String tableName) {
        String sql = "INSERT INTO " + tableName + " (id_usuario) VALUES (?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setPrimerApellido(rs.getString("primer_apellido"));
        u.setSegundoApellido(rs.getString("segundo_apellido"));
        u.setEmail(rs.getString("email"));
        u.setClave(rs.getString("clave"));
        u.setEstado(rs.getInt("estado"));
        return u;
    }
}
