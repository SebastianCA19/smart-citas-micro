package smartcitas.users;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import smartcitas.users.controller.*;
import smartcitas.users.filters.CorsFilter;

public class UserServiceApplication {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8081);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);
        FilterDef corsFilterDef = new FilterDef();
        corsFilterDef.setFilterName("CorsFilter");
        corsFilterDef.setFilter(new CorsFilter());
        context.addFilterDef(corsFilterDef);

        FilterMap corsFilterMap = new FilterMap();
        corsFilterMap.setFilterName("CorsFilter");
        corsFilterMap.addURLPattern("/*");
        context.addFilterMap(corsFilterMap);

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
