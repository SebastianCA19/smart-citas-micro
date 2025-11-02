package smartcitas.medical_records.controller;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import smartcitas.medical_records.dto.CreateMedicalRecordDTO;
import smartcitas.medical_records.dto.ResponseMedicalRecordDTO;
import smartcitas.medical_records.service.MedicalRecordService;
import smartcitas.medical_records.service.MedicalRecordServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@WebServlet(name = "MedicalRecordController", urlPatterns = "/api/medical-records/*")
public class MedicalRecordController extends HttpServlet {
    private final Gson gson = new Gson();
    private final MedicalRecordService medicalRecordService = new MedicalRecordServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            List<ResponseMedicalRecordDTO> medicalRecords = medicalRecordService.getAll();
            resp.getWriter().println(gson.toJson(medicalRecords));
        } else {
            int id = Integer.parseInt(path.substring(1));
            ResponseMedicalRecordDTO medicalRecord = medicalRecordService.getById(id);

            if (medicalRecord != null) {
                resp.getWriter().println(gson.toJson(medicalRecord));
            } else {
                resp.setStatus(404);
                resp.getWriter().println("{\"error\":\"Historia clinica no encontrado\"}");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        CreateMedicalRecordDTO createDTO = gson.fromJson(
                new BufferedReader(new InputStreamReader(req.getInputStream())),
                CreateMedicalRecordDTO.class);

        int id = medicalRecordService.create(createDTO);

        if (id > 0) {
            resp.getWriter().println("{\"message\":\"Historia clinica creado exitosamente\", \"id\":" + id + "}");
        } else {
            resp.setStatus(400);
            resp.getWriter().println("{\"error\":\"Error al crear el historia clinica\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Debe especificar el id del historia clinica\"}");
            return;
        }

        int id = Integer.parseInt(path.substring(1));
        CreateMedicalRecordDTO updateDTO = gson.fromJson(
                new BufferedReader(new InputStreamReader(req.getInputStream())),
                CreateMedicalRecordDTO.class);

        int rowsAffected = medicalRecordService.update(id, updateDTO);

        if (rowsAffected > 0) {
            resp.getWriter().println("{\"message\":\"Historia clinica actualizado correctamente\"}");
        } else {
            resp.setStatus(404);
            resp.getWriter().println("{\"error\":\"Historia clinica no encontrado\"}");
        }
    }
}
