package smartcitas.medical_records;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import smartcitas.medical_records.controller.MedicalRecordController;

public class MedicalRecordServiceApplication {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8083);
        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        Tomcat.addServlet(context, "medicalRecords", new MedicalRecordController());

        context.addServletMappingDecoded("/api/medicalrecords/*", "medicalRecords");

        tomcat.start();
        tomcat.getServer().await();
    }
}