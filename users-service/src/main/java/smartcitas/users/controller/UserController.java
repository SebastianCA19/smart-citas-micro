package smartcitas.users.controller;

import com.google.gson.Gson;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import smartcitas.users.controller.dto.UserDto;
import smartcitas.users.model.User;
import smartcitas.users.model.dao.UserDao;
import smartcitas.users.model.dao.UserDaoPostgres;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserController", urlPatterns = "/api/users/*")
public class UserController extends HttpServlet {

    private final Gson gson = new Gson();
    private final UserDao userDao = new UserDaoPostgres();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if(path == null || path.equals("/")){
            List<User> users = userDao.getAll();
            resp.getWriter().println(gson.toJson(users));
        }else{
            int id =  Integer.parseInt(path.substring(1));
            User user = userDao.findById(id);

            if(user != null){
                resp.getWriter().println(gson.toJson(user));
            }else{
                resp.setStatus(404);
                resp.getWriter().println("{\"error\":\"Usuario no encontrado\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if (path != null && path.equals("/login")) {
            Map<String, String> body = gson.fromJson(new BufferedReader(new InputStreamReader(req.getInputStream())), Map.class);

            String email = body.get("email");
            String password = body.get("clave");

            User user = userDao.login(email, password);

            if (user != null) {
                UserDto userDto = new UserDto(
                        user.getIdUsuario(), user.getNombre(), user.getPrimerApellido(), user.getSegundoApellido(), user.getEmail(), user.getEstado());

                resp.getWriter().println(gson.toJson(userDto));
            } else {
                resp.setStatus(401);
                resp.getWriter().println("{\"error\":\"Credenciales inválidas\"}");
            }
            return;
        }

        User user = gson.fromJson(new BufferedReader(new InputStreamReader(req.getInputStream())), User.class);
        user.setEstado(1);

        if(userDao.insert(user) > 0){
            resp.getWriter().println("{\"message\":\"Usuario registrado exitosamente\"}");
        }else{
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")){
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Debe especificar el id del usuario\"}");
            return;
        }

        int id = Integer.parseInt(path.substring(1));
        User user = gson.fromJson(new BufferedReader(new InputStreamReader(req.getInputStream())), User.class);
        user.setIdUsuario(id);

        if(userDao.update(user)){
            resp.getWriter().println("{\"message\":\"Usuario actualizado correctamente\"}");
        }else{
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        int id = Integer.parseInt(path.substring(1));

        if(userDao.delete(id)){
            resp.getWriter().println("{\"message\":\"Usuario eliminado correctamente\"}");
        }else{
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Usuario no encontrado\"}");
        }
    }
}

