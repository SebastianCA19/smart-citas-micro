package smartcitas.users.controller;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "DoctorController", urlPatterns = {"/api/doctors/*"})
public class DoctorController extends BaseTypeController {
    @Override
    protected String getTableName() {
        return "medicos";
    }
}
