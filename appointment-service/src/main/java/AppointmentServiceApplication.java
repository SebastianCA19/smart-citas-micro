import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import presentation.AppointmentController;

public class AppointmentServiceApplication{
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        Tomcat.addServlet(context, "appointmentService", new AppointmentController());

        context.addServletMappingDecoded("/api/appointment/*", "appointment");

        tomcat.start();
        tomcat.getServer().await();
    }
}
