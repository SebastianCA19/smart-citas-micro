import filters.CorsFilter;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import presentation.AppointmentController;

public class AppointmentServiceApplication{
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8082);
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

        Tomcat.addServlet(context, "appointmentService", new AppointmentController());

        context.addServletMappingDecoded("/api/appointment/*", "appointmentService");

        tomcat.start();
        tomcat.getServer().await();
    }
}
