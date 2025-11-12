package persistence;

import estructura.ListaEsp;
import model.Place;

public interface PlaceDao {
    ListaEsp<Place> getAll();
    Place getById(int id);
}