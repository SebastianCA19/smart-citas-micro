package smartcitas.citas.service;

import smartcitas.citas.dao.CitaDao;
import smartcitas.citas.dao.FactoryCitaDao;
import smartcitas.citas.dto.CitaDto;
import smartcitas.citas.model.Cita;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Cita management
 * @author edavi
 */
public class CitaService {

    private final FactoryCitaDao factory = FactoryCitaDao.getInstance();

    public Cita crearCita(CitaDto dto) {
        CitaDao dao = factory.getCitaDao("POSTGRES");
        Cita cita = new Cita(dto.idTipoCita(), dto.idLugar(), dto.idProcedimiento(),
                dto.idMedico(), dto.idEnfermero(), dto.idPaciente(), dto.fecha());

        int result = dao.crear(cita);
        if (result > 0) {
            return cita;
        }
        throw new RuntimeException("Failed to create appointment");
    }

    public List<Cita> obtenerTodasLasCitas() {
        CitaDao dao = factory.getCitaDao("POSTGRES");
        return dao.obtenerTodos();
    }

    public Optional<Cita> obtenerCitaPorId(Integer id) {
        CitaDao dao = factory.getCitaDao("POSTGRES");
        return dao.obtenerPorId(id);
    }

    public boolean actualizarCita(Cita cita) {
        if (cita.getId() == null) {
            throw new IllegalArgumentException("Cita ID cannot be null for update");
        }
        CitaDao dao = factory.getCitaDao("POSTGRES");
        return dao.editar(cita);
    }

    public boolean eliminarCita(Integer id) {
        CitaDao dao = factory.getCitaDao("POSTGRES");
        return dao.eliminarPorId(id);
    }

    public Optional<Cita> buscarCitaPorNombreYFecha(String nombre, LocalDate fecha) {
        CitaDao dao = factory.getCitaDao("POSTGRES");
        return dao.obtenerPorNombreYFecha(nombre, fecha);
    }

    public boolean eliminarCitaPorNombreYFecha(String nombre, LocalDate fecha) {
        CitaDao dao = factory.getCitaDao("POSTGRES");
        return dao.eliminarPorNombreYFecha(nombre, fecha);
    }
}