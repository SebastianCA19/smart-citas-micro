package smartcitas.medical_records.controller;

import com.google.gson.Gson;
import estructura.ListaEsp;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import smartcitas.medical_records.dto.CreateMedicalRecordDTO;
import smartcitas.medical_records.dto.ResponseMedicalRecordDTO;
import smartcitas.medical_records.dto.MedicalRecordWithDetailsDTO;
import smartcitas.medical_records.service.MedicalRecordService;
import smartcitas.medical_records.service.MedicalRecordServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MedicalRecordController", urlPatterns = "/api/medicalrecords/*")
public class MedicalRecordController extends HttpServlet {
    private final Gson gson = new Gson();
    private final MedicalRecordService medicalRecordService = new MedicalRecordServiceImpl();

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
        String path = req.getPathInfo();

        // GET /api/medicalrecords/details - Get all records with details
        if (path != null && path.equals("/details")) {
            ListaEsp<MedicalRecordWithDetailsDTO> records = medicalRecordService.getAllWithDetails();
            resp.getWriter().println(gson.toJson(toList(records)));
        }

        // GET /api/medicalrecords/appointment/{id}/details - Get appointment record
        else if (path != null && path.contains("/appointment/") && path.endsWith("/details")){
            String idAppo = path.substring(path.indexOf("/appointment/") + 13, path.indexOf("/details"));
            int id = Integer.parseInt(idAppo);
            ListaEsp<MedicalRecordWithDetailsDTO> record = medicalRecordService.getByAppointmentId(id);
            resp.getWriter().println(gson.toJson(toList(record)));
        }

        // GET /api/medicalrecords/patient/{cedula}/details - Get patient records with details
        else if (path != null && path.contains("/patient/") && path.endsWith("/details")) {
            String cedulaStr = path.substring(path.indexOf("/patient/") + 9, path.indexOf("/details"));
            int cedula = Integer.parseInt(cedulaStr);
            ListaEsp<MedicalRecordWithDetailsDTO> records = medicalRecordService.getByPatientIdWithDetails(cedula);
            resp.getWriter().println(gson.toJson(toList(records)));
        }
        // GET /api/medicalrecords/doctor/{cedula}/details - Get doctor records with details
        else if (path != null && path.contains("/doctor/") && path.endsWith("/details")) {
            String cedulaStr = path.substring(path.indexOf("/doctor/") + 8, path.indexOf("/details"));
            int cedula = Integer.parseInt(cedulaStr);
            ListaEsp<MedicalRecordWithDetailsDTO> records = medicalRecordService.getByDoctorIdWithDetails(cedula);
            resp.getWriter().println(gson.toJson(toList(records)));
        }
        // GET /api/medicalrecords/patient/{cedula} - Get records by patient ID
        else if (path != null && path.startsWith("/patient/")) {
            int cedula = Integer.parseInt(path.substring(9));
            ListaEsp<ResponseMedicalRecordDTO> records = medicalRecordService.getByPatientId(cedula);
            resp.getWriter().println(gson.toJson(toList(records)));
        }
        // GET /api/medicalrecords/doctor/{cedula} - Get records by doctor ID
        else if (path != null && path.startsWith("/doctor/")) {
            int cedula = Integer.parseInt(path.substring(8));
            ListaEsp<ResponseMedicalRecordDTO> records = medicalRecordService.getByDoctorId(cedula);
            resp.getWriter().println(gson.toJson(toList(records)));
        }
        // GET /api/medicalrecords/{id}/details - Get single record with details
        else if (path != null && path.contains("/details")) {
            String idStr = path.substring(1, path.indexOf("/details"));
            int id = Integer.parseInt(idStr);
            MedicalRecordWithDetailsDTO record = medicalRecordService.getByIdWithDetails(id);

            if (record != null) {
                resp.getWriter().println(gson.toJson(record));
            } else {
                resp.setStatus(404);
                resp.getWriter().println("{\"error\":\"Historia clínica no encontrada\"}");
            }
        }
        // GET /api/medicalrecords/ - Get all records
        else if (path == null || path.equals("/")) {
            ListaEsp<ResponseMedicalRecordDTO> medicalRecords = medicalRecordService.getAll();
            resp.getWriter().println(gson.toJson(toList(medicalRecords)));
        }
        // GET /api/medicalrecords/{id} - Get single record by ID
        else {
            int id = Integer.parseInt(path.substring(1));
            ResponseMedicalRecordDTO medicalRecord = medicalRecordService.getById(id);

            if (medicalRecord != null) {
                resp.getWriter().println(gson.toJson(medicalRecord));
            } else {
                resp.setStatus(404);
                resp.getWriter().println("{\"error\":\"Historia clínica no encontrada\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        CreateMedicalRecordDTO createDTO = gson.fromJson(
                new BufferedReader(new InputStreamReader(req.getInputStream())),
                CreateMedicalRecordDTO.class);

        System.out.println("Dto: " + createDTO);

        int id = medicalRecordService.create(createDTO);

        if (id > 0) {
            resp.getWriter().println("{\"message\":\"Historia clínica creada exitosamente\", \"id\":" + id + "}");
        } else {
            resp.setStatus(400);
            resp.getWriter().println("{\"error\":\"Error al crear la historia clínica\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Debe especificar el id de la historia clínica\"}");
            return;
        }

        int id = Integer.parseInt(path.substring(1));
        CreateMedicalRecordDTO updateDTO = gson.fromJson(
                new BufferedReader(new InputStreamReader(req.getInputStream())),
                CreateMedicalRecordDTO.class);

        int rowsAffected = medicalRecordService.update(id, updateDTO);

        if (rowsAffected > 0) {
            resp.getWriter().println("{\"message\":\"Historia clínica actualizada correctamente\"}");
        } else {
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Historia clínica no encontrada\"}");
        }
    }
}