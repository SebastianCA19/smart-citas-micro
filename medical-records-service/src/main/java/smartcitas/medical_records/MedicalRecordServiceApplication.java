package smartcitas.medical_records;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import smartcitas.medical_records.controller.MedicalRecordController;
import smartcitas.medical_records.filters.CorsFilter;

public class MedicalRecordServiceApplication {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8083);
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

        Tomcat.addServlet(context, "medicalRecords", new MedicalRecordController());

        context.addServletMappingDecoded("/api/medicalrecords/*", "medicalRecords");

        tomcat.start();
        tomcat.getServer().await();
    }
}