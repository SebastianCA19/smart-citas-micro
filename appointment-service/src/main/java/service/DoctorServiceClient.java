package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DoctorServiceClient {

    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private final Gson gson = new Gson();

    public String getDoctorFullName(int doctorId) {
        try {
            URL url = new URL(USER_SERVICE_URL + "/api/doctors");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                // Parse JSON array
                JsonArray doctors = gson.fromJson(response.toString(), JsonArray.class);

                // Find the doctor by ID
                for (int i = 0; i < doctors.size(); i++) {
                    JsonObject doctor = doctors.get(i).getAsJsonObject();
                    if (doctor.get("cedula").getAsInt() == doctorId) {
                        String nombre = doctor.get("nombre").getAsString();
                        String primerApellido = doctor.get("primerApellido").getAsString();
                        String segundoApellido = doctor.get("segundoApellido").getAsString();

                        return nombre + " " + primerApellido + " " + segundoApellido;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Doctor no encontrado";
    }
}