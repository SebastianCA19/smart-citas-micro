package service;

import estructura.ListaEsp;
import model.*;
import persistence.*;
import persistence.impl.*;
import presentation.dto.AppointmentDto;
import presentation.dto.AppointmentWithDetailsDto;

public class AppointmentService {

    private final AppointmentFactoryDao appointmentFactoryDao = AppointmentFactoryDao.getInstance();
    private final DoctorServiceClient doctorServiceClient = new DoctorServiceClient();
    private final AppointmentTypeDaoPostgres appointmentTypeDao = new AppointmentTypeDaoPostgres();
    private final ProcedureDaoPostgres procedureDao = new ProcedureDaoPostgres();
    private final PlaceDaoPostgres placeDao = new PlaceDaoPostgres();

    public int saveAppointment(AppointmentDto appointmentDto) {
        AppointmentDao dao = appointmentFactoryDao.getDao("postgres");
        Appointment appo = new Appointment(
                null,
                appointmentDto.idAppointmentType(),
                appointmentDto.idPlace(),
                appointmentDto.idNurse(),
                appointmentDto.idProcedure(),
                appointmentDto.idDoctor(),
                appointmentDto.idPatient(),
                appointmentDto.date()
        );

        return dao.save(appo);
    }

    public ListaEsp<AppointmentDto> getAllAppointments() {
        AppointmentDao dao = appointmentFactoryDao.getDao("postgres");
        ListaEsp<Appointment> appointments = dao.getAll();
        ListaEsp<AppointmentDto> appointmentDtos = new ListaEsp<>();

        for (int i = 0; i < appointments.numElementos(); i++) {
            Appointment app = appointments.obtener(i);
            AppointmentDto dto = new AppointmentDto(
                    app.getId(),
                    app.getIdAppointmentType(),
                    app.getIdPlace(),
                    app.getIdProcedure(),
                    app.getIdDoctor(),
                    app.getIdNurse(),
                    app.getIdPatient(),
                    app.getDate()
            );
            appointmentDtos.agregar(dto);
        }

        return appointmentDtos;
    }

    public AppointmentDto getAppointmentById(int id) {
        AppointmentDao dao = appointmentFactoryDao.getDao("postgres");
        Appointment appointment = dao.getById(id);

        if (appointment != null) {
            return new AppointmentDto(
                    appointment.getId(),
                    appointment.getIdAppointmentType(),
                    appointment.getIdPlace(),
                    appointment.getIdProcedure(),
                    appointment.getIdDoctor(),
                    appointment.getIdNurse(),
                    appointment.getIdPatient(),
                    appointment.getDate()
            );
        }

        return null;
    }

    public int updateAppointment(int id, AppointmentDto appointmentDto) {
        AppointmentDao dao = appointmentFactoryDao.getDao("postgres");
        Appointment existingAppointment = dao.getById(id);

        if (existingAppointment != null) {
            existingAppointment.setIdAppointmentType(appointmentDto.idAppointmentType());
            existingAppointment.setIdPlace(appointmentDto.idPlace());
            existingAppointment.setIdProcedure(appointmentDto.idProcedure());
            existingAppointment.setIdDoctor(appointmentDto.idDoctor());
            existingAppointment.setIdNurse(appointmentDto.idNurse());
            existingAppointment.setIdPatient(appointmentDto.idPatient());
            existingAppointment.setDate(appointmentDto.date());

            return dao.update(existingAppointment);
        }

        return 0;
    }

    public void deleteAppointment(int id) {
        AppointmentDao dao = appointmentFactoryDao.getDao("postgres");
        dao.deleteById(id);
    }

    public AppointmentWithDetailsDto getAppointmentWithDetails(int id) {
        AppointmentDaoPostgres dao = (AppointmentDaoPostgres) appointmentFactoryDao.getDao("postgres");
        Appointment appointment = dao.getById(id);

        if (appointment != null) {
            // Get names from database and external service
            String doctorName = doctorServiceClient.getDoctorFullName(appointment.getIdDoctor());
            String appointmentTypeName = dao.getAppointmentTypeName(appointment.getIdAppointmentType());
            String procedureName = dao.getProcedureName(appointment.getIdProcedure());
            String placeName = dao.getPlaceName(appointment.getIdPlace());

            return new AppointmentWithDetailsDto(
                    appointment.getId(),
                    appointment.getIdAppointmentType(),
                    appointmentTypeName,
                    appointment.getIdPlace(),
                    placeName,
                    appointment.getIdProcedure(),
                    procedureName,
                    appointment.getIdDoctor(),
                    doctorName,
                    appointment.getIdNurse(),
                    appointment.getIdPatient(),
                    appointment.getDate()
            );
        }

        return null;
    }

    public ListaEsp<AppointmentWithDetailsDto> getAllAppointmentsWithDetails() {
        AppointmentDao dao = appointmentFactoryDao.getDao("postgres");
        ListaEsp<Appointment> appointments = dao.getAll();
        ListaEsp<AppointmentWithDetailsDto> detailedAppointments = new ListaEsp<>();

        for (int i = 0; i < appointments.numElementos(); i++) {
            Appointment app = appointments.obtener(i);
            AppointmentWithDetailsDto detailed = getAppointmentWithDetails(app.getId());
            if (detailed != null) {
                detailedAppointments.agregar(detailed);
            }
        }

        return detailedAppointments;
    }

    public ListaEsp<AppointmentWithDetailsDto> getAppointmentsWithDetailsByPatientId(int patientId) {
        AppointmentDaoPostgres dao = (AppointmentDaoPostgres) appointmentFactoryDao.getDao("postgres");
        ListaEsp<Appointment> appointments = dao.getByPatientId(patientId);
        ListaEsp<AppointmentWithDetailsDto> detailedAppointments = new ListaEsp<>();

        for (int i = 0; i < appointments.numElementos(); i++) {
            Appointment app = appointments.obtener(i);
            AppointmentWithDetailsDto detailed = getAppointmentWithDetails(app.getId());
            if (detailed != null) {
                detailedAppointments.agregar(detailed);
            }
        }

        return detailedAppointments;
    }

    public ListaEsp<AppointmentWithDetailsDto> getAppointmentsWithDetailsByDoctorId(int doctorId) {
        AppointmentDaoPostgres dao = (AppointmentDaoPostgres) appointmentFactoryDao.getDao("postgres");
        ListaEsp<Appointment> appointments = dao.getByDoctorId(doctorId);
        ListaEsp<AppointmentWithDetailsDto> detailedAppointments = new ListaEsp<>();

        for (int i = 0; i < appointments.numElementos(); i++) {
            Appointment app = appointments.obtener(i);
            AppointmentWithDetailsDto detailed = getAppointmentWithDetails(app.getId());
            if (detailed != null) {
                detailedAppointments.agregar(detailed);
            }
        }

        return detailedAppointments;
    }

    public ListaEsp<AppointmentWithDetailsDto> getAppointmentsByDoctorIdAndDate(int doctorId, String date) {
        AppointmentDaoPostgres dao = (AppointmentDaoPostgres) appointmentFactoryDao.getDao("postgres");
        ListaEsp<Appointment> appointments = dao.getByDoctorIdAndDate(doctorId, date);
        ListaEsp<AppointmentWithDetailsDto> detailedAppointments = new ListaEsp<>();

        for (int i = 0; i < appointments.numElementos(); i++) {
            Appointment app = appointments.obtener(i);
            AppointmentWithDetailsDto detailed = getAppointmentWithDetails(app.getId());
            if (detailed != null) {
                detailedAppointments.agregar(detailed);
            }
        }

        return detailedAppointments;
    }

    public ListaEsp<AppointmentType> getAllAppointmentTypes() {
        return appointmentTypeDao.getAll();
    }

    public ListaEsp<Procedure> getAllProcedures() {
        return procedureDao.getAll();
    }

    public ListaEsp<Place> getAllPlaces() {
        return placeDao.getAll();
    }
}