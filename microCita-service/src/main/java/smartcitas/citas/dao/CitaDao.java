package smartcitas.citas.dao;

import smartcitas.citas.model.Cita;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Cita entity
 * @author edavi
 */
public interface CitaDao {

    List<Cita> obtenerTodos();

    Optional<Cita> obtenerPorId(Integer id);

    int crear(Cita cita);

    boolean editar(Cita cita);

    boolean eliminarPorId(Integer id);

    Optional<Cita> obtenerPorNombreYFecha(String nombre, LocalDate fecha);

    boolean eliminarPorNombreYFecha(String nombre, LocalDate fecha);
}