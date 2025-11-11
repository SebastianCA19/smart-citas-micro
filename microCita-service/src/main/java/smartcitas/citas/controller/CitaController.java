package smartcitas.citas.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import smartcitas.citas.dto.CitaDto;
import smartcitas.citas.model.Cita;
import smartcitas.citas.service.CitaService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * HTTP Servlet for Cita REST API endpoints
 */
public class CitaController extends HttpServlet {

    private final Gson gson = new Gson();
    private final CitaService citaService = new CitaService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String path = req.getPathInfo();

            if (path == null || path.equals("/")) {
                // GET /api/citas - List all appointments
                List<Cita> citas = citaService.obtenerTodasLasCitas();
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().println(gson.toJson(citas));
            } else {
                // GET /api/citas/{id} - Get specific appointment
                try {
                    Integer id = Integer.parseInt(path.substring(1));
                    Optional<Cita> cita = citaService.obtenerCitaPorId(id);

                    if (cita.isPresent()) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        resp.getWriter().println(gson.toJson(cita.get()));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().println(gson.toJson(java.util.Map.of(
                            "error", "Appointment not found",
                            "message", "Cita con ID " + id + " no encontrada"
                        )));
                    }
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().println(gson.toJson(java.util.Map.of(
                        "error", "Invalid ID format",
                        "message", "El ID debe ser un número entero"
                    )));
                }
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(gson.toJson(java.util.Map.of(
                "error", "Internal server error",
                "message", "Error al procesar la solicitud: " + e.getMessage()
            )));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            CitaDto citaDto = gson.fromJson(new BufferedReader(new InputStreamReader(req.getInputStream())), CitaDto.class);

            // Validate required fields
            if (citaDto.idTipoCita() == null || citaDto.idLugar() == null ||
                citaDto.idProcedimiento() == null || citaDto.idPaciente() == null ||
                citaDto.idMedico() == null || citaDto.idEnfermero() == null ||
                citaDto.fecha() == null) {

                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(java.util.Map.of(
                    "error", "Missing required fields",
                    "message", "Todos los campos son obligatorios: idTipoCita, idLugar, idProcedimiento, idPaciente, idMedico, idEnfermero, fecha"
                )));
                return;
            }

            Cita createdCita = citaService.crearCita(citaDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().println(gson.toJson(createdCita));

        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(java.util.Map.of(
                "error", "Invalid JSON format",
                "message", "El formato JSON de la solicitud es inválido"
            )));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(java.util.Map.of(
                "error", "Creation failed",
                "message", e.getMessage()
            )));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(gson.toJson(java.util.Map.of(
                "error", "Internal server error",
                "message", "Error al procesar la solicitud: " + e.getMessage()
            )));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String path = req.getPathInfo();

            if (path == null || path.equals("/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(java.util.Map.of(
                    "error", "Missing ID",
                    "message", "Debe especificar el ID de la cita en la URL: /api/citas/{id}"
                )));
                return;
            }

            try {
                Integer id = Integer.parseInt(path.substring(1));
                Cita cita = gson.fromJson(new BufferedReader(new InputStreamReader(req.getInputStream())), Cita.class);
                cita.setId(id);

                if (citaService.actualizarCita(cita)) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println(gson.toJson(java.util.Map.of(
                        "message", "Appointment updated successfully",
                        "cita", cita
                    )));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(gson.toJson(java.util.Map.of(
                        "error", "Update failed",
                        "message", "Cita con ID " + id + " no encontrada o no se pudo actualizar"
                    )));
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(java.util.Map.of(
                    "error", "Invalid ID format",
                    "message", "El ID debe ser un número entero"
                )));
            }
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println(gson.toJson(java.util.Map.of(
                "error", "Invalid JSON format",
                "message", "El formato JSON de la solicitud es inválido"
            )));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(gson.toJson(java.util.Map.of(
                "error", "Internal server error",
                "message", "Error al procesar la solicitud: " + e.getMessage()
            )));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String path = req.getPathInfo();

            if (path == null || path.equals("/")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(java.util.Map.of(
                    "error", "Missing ID",
                    "message", "Debe especificar el ID de la cita en la URL: /api/citas/{id}"
                )));
                return;
            }

            try {
                Integer id = Integer.parseInt(path.substring(1));

                if (citaService.eliminarCita(id)) {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().println(gson.toJson(java.util.Map.of(
                        "error", "Delete failed",
                        "message", "Cita con ID " + id + " no encontrada"
                    )));
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println(gson.toJson(java.util.Map.of(
                    "error", "Invalid ID format",
                    "message", "El ID debe ser un número entero"
                )));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println(gson.toJson(java.util.Map.of(
                "error", "Internal server error",
                "message", "Error al procesar la solicitud: " + e.getMessage()
            )));
        }
    }
}