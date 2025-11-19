package smartcitas.users.controller;

import com.google.gson.Gson;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import smartcitas.users.controller.dto.UserDTORecord;
import smartcitas.users.model.User;
import smartcitas.users.model.dao.DAOFactory;
import smartcitas.users.model.dao.UserDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@WebServlet(name = "UserController", urlPatterns = "/api/users/*")
public class UserController extends HttpServlet {

    private final Gson gson = new Gson();
    private final DAOFactory fac = DAOFactory.getFactory();
    private final UserDao userDao = fac.getUserDao("postgres");


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if(path == null || path.equals("/")){
            List<User> users = userDao.getAll();
            resp.getWriter().println(gson.toJson(users));
        }else{
            int cedula = Integer.parseInt(path.substring(1));
            User user = userDao.findById(cedula);

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
                UserDTORecord userDto = new UserDTORecord(
                        user.getCedula(), user.getNombre(), user.getPrimerApellido(), user.getSegundoApellido(), user.getEmail(), user.getEstado());

                resp.getWriter().println(gson.toJson(userDto));
            } else {
                resp.setStatus(401);
                resp.getWriter().println("{\"error\":\"Credenciales inválidas\"}");
            }
            return;
        }

        User user = gson.fromJson(new BufferedReader(new InputStreamReader(req.getInputStream())), User.class);

        // Validate that cedula is provided
        if (user.getCedula() == 0) {
            resp.setStatus(400);
            resp.getWriter().println("{\"error\":\"La cédula es requerida\"}");
            return;
        }

        user.setEstado(1);

        int cedula = userDao.insert(user);
        if(cedula > 0){
            resp.getWriter().println("{\"message\":\"Usuario registrado exitosamente\", \"cedula\":" + cedula + "}");
        }else{
            resp.setStatus(400);
            resp.getWriter().println("{\"error\":\"Error al registrar usuario. La cédula podría estar duplicada\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")){
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Debe especificar la cédula del usuario\"}");
            return;
        }

        int cedula = Integer.parseInt(path.substring(1));
        User user = gson.fromJson(new BufferedReader(new InputStreamReader(req.getInputStream())), User.class);
        user.setCedula(cedula);

        if(userDao.update(user)){
            resp.getWriter().println("{\"message\":\"Usuario actualizado correctamente\"}");
        }else{
            resp.setStatus(400);
            resp.getWriter().println("{\"error\":\"Error al actualizar usuario\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        int cedula = Integer.parseInt(path.substring(1));

        if(userDao.delete(cedula)){
            resp.getWriter().println("{\"message\":\"Usuario eliminado correctamente\"}");
        }else{
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Usuario no encontrado\"}");
        }
    }
}