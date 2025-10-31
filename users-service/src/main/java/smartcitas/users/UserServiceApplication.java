package smartcitas.users;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import smartcitas.users.controller.*;

public class UserServiceApplication {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        Tomcat.addServlet(context, "userServlet", new UserController());
        context.addServletMappingDecoded("/api/users/*", "userServlet");

        Tomcat.addServlet(context, "patientServlet", new PatientController());
        context.addServletMappingDecoded("/api/patients/*", "patientServlet");

        Tomcat.addServlet(context, "doctorServlet", new DoctorController());
        context.addServletMappingDecoded("/api/doctors/*", "doctorServlet");

        Tomcat.addServlet(context, "nurseServlet", new NurseController());
        context.addServletMappingDecoded("/api/nurse/*", "nurseServlet");

        Tomcat.addServlet(context, "adminServlet", new AdminController());
        context.addServletMappingDecoded("/api/admins/*", "adminServlet");

        tomcat.start();
        tomcat.getServer().await();
    }
}
