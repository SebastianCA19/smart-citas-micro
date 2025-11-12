package persistence;

import estructura.ListaEsp;
import model.Procedure;

public interface ProcedureDao {
    ListaEsp<Procedure> getAll();
    Procedure getById(int id);
}