package smartcitas.users.controller;

import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "PatientController", urlPatterns = {"/api/patients/*"})
public class PatientController extends BaseTypeController {
    @Override
    protected String getTableName() {
        return "pacientes";
    }
}
