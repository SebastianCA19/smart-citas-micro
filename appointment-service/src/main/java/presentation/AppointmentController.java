package presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import estructura.ListaEsp;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import presentation.dto.AppointmentDto;
import presentation.dto.AppointmentWithDetailsDto;
import service.AppointmentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AppointmentController", urlPatterns = "/api/appointment/*")
public class AppointmentController extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final AppointmentService appointmentService = new AppointmentService();

    // Helper method to convert ListaEsp to ArrayList
    private <T> List<T> toList(ListaEsp<T> listaEsp) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < listaEsp.numElementos(); i++) {
            list.add(listaEsp.obtener(i));
        }
        return list;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        // GET /api/appointment/catalogs - Get all catalogs
        if (path != null && path.equals("/catalogs")) {
            ListaEsp<AppointmentType> types = appointmentService.getAllAppointmentTypes();
            ListaEsp<Procedure> procedures = appointmentService.getAllProcedures();
            ListaEsp<Place> places = appointmentService.getAllPlaces();

            String jsonResponse = String.format(
                    "{\"appointmentTypes\":%s,\"procedures\":%s,\"places\":%s}",
                    gson.toJson(toList(types)),
                    gson.toJson(toList(procedures)),
                    gson.toJson(toList(places))
            );

            resp.getWriter().println(jsonResponse);
        }
        // GET /api/appointment/details - Get all appointments with details
        else if (path != null && path.equals("/details")) {
            ListaEsp<AppointmentWithDetailsDto> appointments = appointmentService.getAllAppointmentsWithDetails();
            resp.getWriter().println(gson.toJson(toList(appointments)));
        }
        // GET /api/appointment/patient/{cedula}/details - Get appointments by patient ID with details
        else if (path != null && path.contains("/patient/") && path.endsWith("/details")) {
            String cedulaStr = path.substring(path.indexOf("/patient/") + 9, path.indexOf("/details"));
            int cedula = Integer.parseInt(cedulaStr);
            ListaEsp<AppointmentWithDetailsDto> appointments = appointmentService.getAppointmentsWithDetailsByPatientId(cedula);
            resp.getWriter().println(gson.toJson(toList(appointments)));
        }
        // GET /api/appointment/doctor/{cedula}/details  (sin fecha)
// GET /api/appointment/doctor/{cedula}/details?date=YYYY-MM-DD  (con fecha)
        else if (path != null && path.contains("/doctor/") && path.endsWith("/details")) {

            // Extraer cédula
            String cedulaStr = path.substring(
                    path.indexOf("/doctor/") + 8,
                    path.indexOf("/details")
            );
            int cedula = Integer.parseInt(cedulaStr);

            // Leer parámetro date (puede venir o no)
            String date = req.getParameter("date");

            ListaEsp<AppointmentWithDetailsDto> appointments;

            if (date != null && !date.isEmpty()) {
                // Caso con fecha
                appointments = appointmentService.getAppointmentsByDoctorIdAndDate(cedula, date);
            } else {
                // Caso sin fecha
                appointments = appointmentService.getAppointmentsWithDetailsByDoctorId(cedula);
            }

            // Respuesta JSON
            resp.getWriter().println(gson.toJson(toList(appointments)));
        }

        // GET /api/appointment/{id}/details - Get appointment by ID with details
        else if (path != null && path.contains("/details")) {
            String idStr = path.substring(1, path.indexOf("/details"));
            int id = Integer.parseInt(idStr);
            AppointmentWithDetailsDto appointment = appointmentService.getAppointmentWithDetails(id);

            if (appointment != null) {
                resp.getWriter().println(gson.toJson(appointment));
            } else {
                resp.setStatus(404);
                resp.getWriter().println("{\"error\":\"Cita no encontrada\"}");
            }
        }
        // GET /api/appointment - Get all appointments
        else if (path == null || path.equals("/")) {
            ListaEsp<AppointmentDto> appointments = appointmentService.getAllAppointments();
            resp.getWriter().println(gson.toJson(toList(appointments)));
        }
        // GET /api/appointment/{id} - Get appointment by ID
        else {
            int id = Integer.parseInt(path.substring(1));
            AppointmentDto appointment = appointmentService.getAppointmentById(id);

            if (appointment != null) {
                resp.getWriter().println(gson.toJson(appointment));
            } else {
                resp.setStatus(404);
                resp.getWriter().println("{\"error\":\"Cita no encontrada\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        AppointmentDto appointmentDto = gson.fromJson(
                new BufferedReader(new InputStreamReader(req.getInputStream())),
                AppointmentDto.class);

        int id = appointmentService.saveAppointment(appointmentDto);

        if (id > 0) {
            resp.getWriter().println("{\"message\":\"Cita creada exitosamente\", \"id\":" + id + "}");
        } else {
            resp.setStatus(400);
            resp.getWriter().println("{\"error\":\"Error al crear la cita\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Debe especificar el id de la cita\"}");
            return;
        }

        int id = Integer.parseInt(path.substring(1));
        AppointmentDto appointmentDto = gson.fromJson(
                new BufferedReader(new InputStreamReader(req.getInputStream())),
                AppointmentDto.class);

        int rowsAffected = appointmentService.updateAppointment(id, appointmentDto);

        if (rowsAffected > 0) {
            resp.getWriter().println("{\"message\":\"Cita actualizada correctamente\"}");
        } else {
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Cita no encontrada\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.setStatus(400);
            resp.getWriter().println("{\"error\":\"Debe especificar el id de la cita\"}");
            return;
        }

        int id = Integer.parseInt(path.substring(1));
        appointmentService.deleteAppointment(id);
        resp.getWriter().println("{\"message\":\"Cita eliminada correctamente\"}");
    }
}