package smartcitas.users.controller;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "AdminController", urlPatterns = {"/api/admins/*"})
public class AdminController extends BaseTypeController {
    @Override
    protected String getTableName() {
        return "administrativos";
    }
}
