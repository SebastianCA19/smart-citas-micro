/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.impl;

import estructura.ListaEsp;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import modelo.Cita;
import persistencia.CitaDao;

/**
 *
 * @author edavi
 */
public class CitaDaoPostgres implements CitaDao{
    private static final  Dotenv dotenv = Dotenv.configure()
                        .directory("./src/resources")  
                        .load();
    
    static final String URL = dotenv.get("URL");
    private static final String USER = dotenv.get("USER");
    private static final String PASSWORD = dotenv.get("PASSWORD");
    int res=0;

    public CitaDaoPostgres() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ListaEsp<Cita> obtenerTodos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Cita obtenerPorNombreYFecha(String nombre, LocalDate fecha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminarPorNombreYFecha(String nombre, LocalDate fecha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int crear(Cita cita) {
        String sql = "INSERT INTO autores (idTipoCita, idLugar, idProcedimiento, "
                + "idPaciente, idMedico, fecha) VALUES (?, ?, ?, ?, ?, ?)";
        int res=0;
        try (
             Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            
            ps.setInt(1,cita.getIdTipoCita());
            ps.setInt(2,cita.getIdLugar());
            ps.setInt(3, cita.getIdProcedimiento());
            ps.setInt(4, cita.getIdPaciente());
            ps.setInt(5, cita.getIdMedico());
            ps.setObject(6, cita.getFecha());

            res= ps.executeUpdate();
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Que pasa aqui...");
        }
        return res;
    }

    @Override
    public void editar(LocalDate fecha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
