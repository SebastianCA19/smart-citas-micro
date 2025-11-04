/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import estructura.ListaEsp;
import java.time.LocalDate;
import modelo.Cita;

/**
 *
 * @author edavi
 */
public interface CitaDao {
    
    ListaEsp<Cita> obtenerTodos();
    Cita obtenerPorNombreYFecha(String nombre, LocalDate fecha);
    
    void eliminarPorNombreYFecha(String nombre, LocalDate fecha);
    
    int crear(Cita cita);
    
    void editar(LocalDate fecha);
}
