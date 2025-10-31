package smartcitas.users.controller;

import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import smartcitas.users.model.User;
import smartcitas.users.model.dao.UserDao;
import smartcitas.users.model.dao.UserDaoPostgres;

public abstract class BaseTypeController extends HttpServlet {
    protected final UserDao userDao = new UserDaoPostgres();
    protected final Gson gson = new Gson();

    // Cada subclase define su tabla (pacientes, medicos, etc.)
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
                int id = Integer.parseInt(path.substring(1));
                User user = userDao.findById(id);
                resp.getWriter().write(gson.toJson(user));
            } catch (Exception e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        User user = gson.fromJson(reader, User.class);

        int id = userDao.insert(user);
        if (id > 0) {
            userDao.linkUserToType(id, getTableName());
        }

        resp.setContentType("application/json");
        resp.getWriter().write("{\"created\": " + id + "}");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        User user = gson.fromJson(reader, User.class);

        boolean ok = userDao.update(user);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"updated\": " + ok + "}");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        if (path != null && path.length() > 1) {
            int id = Integer.parseInt(path.substring(1));
            boolean ok = userDao.delete(id);
            resp.getWriter().write("{\"deleted\": " + ok + "}");
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debe indicar ID");
        }
    }
}

