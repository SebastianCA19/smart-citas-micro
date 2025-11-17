package service;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserServiceClient {

    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private final Gson gson = new Gson();

    public String getUserFullName(int id) {
        try {
            URL url = new URL(USER_SERVICE_URL + "/api/doctors/" +  id);
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
                conn.disconnect();
                return response.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Doctor no encontrado";
    }
}