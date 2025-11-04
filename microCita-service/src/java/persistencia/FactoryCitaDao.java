/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import persistencia.impl.CitaDaoPostgres;

/**
 *
 * @author edavi
 */
public class FactoryCitaDao {
    
    private static FactoryCitaDao instance;
    
    private FactoryCitaDao(){
        
    }
    
    public static FactoryCitaDao getInstance(){
        if(instance == null){
            instance = new FactoryCitaDao();
        }
        return instance;
    }
    
    public CitaDao getCitaDao(String nombreDb){
        nombreDb = nombreDb.toUpperCase();
        switch (nombreDb) {
            case "POSTGRES":
                return new CitaDaoPostgres();
            default:
                throw new AssertionError();
        }
    }
    
}
