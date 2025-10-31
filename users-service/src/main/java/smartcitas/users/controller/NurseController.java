package smartcitas.users.controller;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "NurseController", urlPatterns = {"/api/nurses/*"})
public class NurseController extends BaseTypeController {
    @Override
    protected String getTableName() {
        return "enfermeros";
    }
}
