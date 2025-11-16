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

@WebServlet(name = "AppointmentController", urlPatterns = "/api/appointment/*")
public class AppointmentController extends HttpServlet {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final AppointmentService appointmentService = new AppointmentService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        // GET /api/appointment/catalogs - Get all catalogs
        if (path != null && path.equals("/catalogs")) {
            ListaEsp<AppointmentType> types = appointmentService.getAllAppointmentTypes();
            ListaEsp<Procedure> procedures = appointmentService.getAllProcedures();
            ListaEsp<Place> places = appointmentService.getAllPlaces();

            String jsonResponse = String.format(
                    "{\"appointmentTypes\":%s,\"procedures\":%s,\"places\":%s}",
                    gson.toJson(types),
                    gson.toJson(procedures),
                    gson.toJson(places)
            );

            resp.getWriter().println(jsonResponse);
        }
        // GET /api/appointment/details - Get all appointments with details
        else if (path != null && path.equals("/details")) {
            ListaEsp<AppointmentWithDetailsDto> appointments = appointmentService.getAllAppointmentsWithDetails();
            resp.getWriter().println(gson.toJson(appointments));
        }
        // GET /api/appointment/patient/{patientId}/details - Get appointments by patient ID with details
        else if (path != null && path.contains("/patient/") && path.endsWith("/details")) {
            String patientIdStr = path.substring(path.indexOf("/patient/") + 9, path.indexOf("/details"));
            int patientId = Integer.parseInt(patientIdStr);
            ListaEsp<AppointmentWithDetailsDto> appointments = appointmentService.getAppointmentsWithDetailsByPatientId(patientId);
            resp.getWriter().println(gson.toJson(appointments));
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
            resp.getWriter().println(gson.toJson(appointments));
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