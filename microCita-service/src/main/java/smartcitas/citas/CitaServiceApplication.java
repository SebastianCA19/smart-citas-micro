package smartcitas.citas;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import smartcitas.citas.controller.CitaController;

/**
 * Main application class for microCita-service with embedded Tomcat
 */
public class CitaServiceApplication {

    private static final int PORT = 8082;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting Cita Service on port " + PORT);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        Tomcat.addServlet(context, "citaServlet", new CitaController());
        context.addServletMappingDecoded("/api/citas/*", "citaServlet");

        tomcat.start();
        System.out.println("Cita Service started successfully on port " + PORT);
        System.out.println("API endpoints available at:");
        System.out.println("  POST   http://localhost:" + PORT + "/api/citas - Create appointment");
        System.out.println("  GET    http://localhost:" + PORT + "/api/citas - List all appointments");
        System.out.println("  GET    http://localhost:" + PORT + "/api/citas/{id} - Get appointment by ID");
        System.out.println("  PUT    http://localhost:" + PORT + "/api/citas/{id} - Update appointment");
        System.out.println("  DELETE http://localhost:" + PORT + "/api/citas/{id} - Delete appointment");

        tomcat.getServer().await();
    }
}