package smartcitas.medical_records.service;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserServiceClient {
    private static final String USER_SERVICE_URL = "http://localhost:8081";
    private final Gson gson = new Gson();

    public String getUserFullName(int cedula){
        try{
            URL url = new URL(USER_SERVICE_URL+"/api/users/"+ cedula);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();

            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer response = new StringBuffer();
                String line;

                while((line = br.readLine()) != null){
                    response.append(line);
                }
                br.close();
                conn.disconnect();

                return response.toString();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return "Usuario no encontrado";
    }
}
