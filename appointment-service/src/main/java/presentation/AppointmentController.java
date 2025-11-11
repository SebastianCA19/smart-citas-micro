package presentation;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "AppointmentController", urlPatterns = "/api/appointment/*")
public class AppointmentController extends HttpServlet {
}
