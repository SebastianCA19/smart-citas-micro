package service;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import model.Appointment;
import persistence.AppointmentDao;
import persistence.AppointmentFactoryDao;
import presentation.dto.AppointmentDto;

public class AppointmentService{

    private AppointmentFactoryDao appointmentFactoryDao = AppointmentFactoryDao.getInstance();

    public int saveAppointment(AppointmentDto appointmentDto) {
        AppointmentDao dao = appointmentFactoryDao.getDao("postgres");
        Appointment appo = new Appointment(appointmentDto.id(), appointmentDto.idAppointmentType(), appointmentDto.idPlace(), appointmentDto.idProcedure(), appointmentDto.idDoctor(), appointmentDto.idNurse(), appointmentDto.idPatient(), appointmentDto.date());

        return dao.save(appo);
    }
}
