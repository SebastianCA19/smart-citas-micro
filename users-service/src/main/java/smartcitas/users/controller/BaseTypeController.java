package smartcitas.users.controller;

import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import smartcitas.users.model.User;
import smartcitas.users.model.dao.DAOFactory;
import smartcitas.users.model.dao.UserDao;

public abstract class BaseTypeController extends HttpServlet {
    protected final DAOFactory fac =  DAOFactory.getFactory();
    protected final UserDao userDao = fac.getUserDao("postgres");
    protected final Gson gson = new Gson();

    protected abstract String getTableName();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");

        if (path == null || path.equals("/")) {
            List<User> users = userDao.getUsersByType(getTableName());
            resp.getWriter().write(gson.toJson(users));
        } else {
            try {
                int cedula = Integer.parseInt(path.substring(1));
                User user = userDao.findById(cedula);

                if (user != null) {
                    resp.getWriter().write(gson.toJson(user));
                } else {
                    resp.setStatus(404);
                    resp.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
                }
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cédula inválida");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        User user = gson.fromJson(reader, User.class);

        // Validate that cedula is provided
        if (user.getCedula() == 0) {
            resp.setStatus(400);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"La cédula es requerida\"}");
            return;
        }

        int cedula = userDao.insert(user);
        if (cedula > 0) {
            userDao.linkUserToType(cedula, getTableName());
            resp.setContentType("application/json");
            resp.getWriter().write("{\"message\":\"Usuario creado exitosamente\", \"cedula\": " + cedula + "}");
        } else {
            resp.setStatus(400);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Error al crear usuario. La cédula podría estar duplicada\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.setStatus(400);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"error\":\"Debe especificar la cédula del usuario\"}");
            return;
        }

        int cedula = Integer.parseInt(path.substring(1));
        BufferedReader reader = req.getReader();
        User user = gson.fromJson(reader, User.class);
        user.setCedula(cedula);

        boolean ok = userDao.update(user);
        resp.setContentType("application/json");
        if (ok) {
            resp.getWriter().write("{\"message\":\"Usuario actualizado correctamente\"}");
        } else {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Error al actualizar usuario\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path != null && path.length() > 1) {
            int cedula = Integer.parseInt(path.substring(1));
            boolean ok = userDao.delete(cedula);
            resp.setContentType("application/json");
            if (ok) {
                resp.getWriter().write("{\"message\":\"Usuario eliminado correctamente\"}");
            } else {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"Usuario no encontrado\"}");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debe indicar cédula");
        }
    }
}